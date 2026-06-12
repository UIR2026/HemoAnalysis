@file:OptIn(ExperimentalForeignApi::class)

package ru.tanexc.hemoanalysis.tool.analysis.impl.inference

import cocoapods.onnxruntime_objc.ORTSession
import cocoapods.onnxruntime_objc.ORTValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.Foundation.NSNumber
import ru.tanexc.hemoanalysis.domain.repository.ModelRepository
import ru.tanexc.hemoanalysis.domain.usecase.GetModelPathUseCase
import ru.tanexc.hemoanalysis.tool.analysis.impl.OrtRuntime
import ru.tanexc.hemoanalysis.tool.analysis.impl.handleThrowingError

internal actual class InferenceHandler : KoinComponent {
    private val getModelPathUseCase: GetModelPathUseCase by inject()
    private val modelRepository: ModelRepository by inject()

    private lateinit var session: ORTSession

    init {
        initSession()
    }

    private fun initSession() {
        val isLatestModel = modelRepository.getCurrentModelInfo()?.isLatest?: false
        if (!isLatestModel || !::session.isInitialized) {
            getModelPathUseCase()?.let {
                session = OrtRuntime.createSession(modelPath = it)
            }
        }
    }

    actual operator fun invoke(tensorImage: FloatArray): InferenceOutput {
        initSession()
        if (!::session.isInitialized) error("Model does not exists")

        val inputTensor = OrtRuntime.createTensor(tensorImage)

        val outputName = handleThrowingError { error ->
            session.outputNamesWithError(error)?.first()
        }

        val outputs = handleThrowingError { error ->
            session.runWithInputs(
                mapOf(session.inputNamesWithError(error)?.first() to inputTensor),
                outputNames = setOf(outputName),
                runOptions = null,
                error = error
            )
        }

        val outputValue = outputs[outputName] as ORTValue
        return outputValue.asInferenceOutput()
    }

    private fun ORTValue.asInferenceOutput(): InferenceOutput {
        val tensorInfo = handleThrowingError { error ->
            tensorTypeAndShapeInfoWithError(error)
        }

        val outputShape = tensorInfo.shape.map { number ->
            (number as NSNumber).longLongValue.toInt()
        }

        val channelCount = outputShape[1]
        val candidateCount = outputShape[2]

        val tensorData = handleThrowingError { error ->
            tensorDataWithError(error)
        }

        val rawFloats = tensorData.bytes!!.reinterpret<FloatVar>()
        var index = 0

        val outputData = Array(1) {
            Array(channelCount) {
                FloatArray(candidateCount)
            }
        }

        for (channel in 0 until channelCount) {
            for (candidate in 0 until candidateCount) {
                outputData[0][channel][candidate] = rawFloats[index++]
            }
        }

        return InferenceOutput(
            channelCount = channelCount,
            candidateCount = candidateCount,
            outputData = outputData
        )
    }
}