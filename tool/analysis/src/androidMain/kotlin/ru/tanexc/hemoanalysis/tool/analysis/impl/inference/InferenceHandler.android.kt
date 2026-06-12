package ru.tanexc.hemoanalysis.tool.analysis.impl.inference

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtSession
import ai.onnxruntime.TensorInfo
import android.content.Context
import android.util.Log
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.tanexc.hemoanalysis.domain.repository.ModelRepository
import ru.tanexc.hemoanalysis.domain.usecase.GetLatestModelFileUseCase
import ru.tanexc.hemoanalysis.tool.analysis.impl.OrtRuntime
import java.nio.FloatBuffer
import kotlin.getValue

internal actual class InferenceHandler : KoinComponent {
    private val getLatestModelFileUseCase: GetLatestModelFileUseCase by inject()
    private val modelRepository: ModelRepository by inject()

    private lateinit var session: OrtSession

    init {
        initSession()
    }

    private fun initSession() {
        val isLatestModel = modelRepository.getCurrentModelInfo()?.isLatest?: false
        if (!isLatestModel || !::session.isInitialized) {
            getLatestModelFileUseCase()?.let { model ->
                session = OrtRuntime.createSession(modelStream = model.inputStream())
            }
        }
    }

    actual operator fun invoke(tensorImage: FloatArray): InferenceOutput {
        initSession()
        if (!::session.isInitialized) error("Model does not exists")

        val inputBuffer: FloatBuffer = FloatBuffer.wrap(tensorImage)
        val tensor = OrtRuntime.createTensor(inputBuffer)

        val output = session.run(mapOf(session.inputNames.first() to tensor))
        return output.asInferenceOutput()
    }

    private fun OrtSession.Result.asInferenceOutput(): InferenceOutput {
        val outputInfo = this[0].info as TensorInfo
        val outputShape = outputInfo.shape

        val channelCount = outputShape[1].toInt()
        val candidateCount = outputShape[2].toInt()

        val outputTensor = this[0] as OnnxTensor
        val outputData = outputTensor.value as Array<Array<FloatArray>>
        return InferenceOutput(
            channelCount = channelCount,
            candidateCount = candidateCount,
            outputData = outputData
        )
    }
}