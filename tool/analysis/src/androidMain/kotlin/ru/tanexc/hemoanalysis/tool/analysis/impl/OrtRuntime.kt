package ru.tanexc.hemoanalysis.tool.analysis.impl

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.MODEL_INPUT_HEIGHT
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.MODEL_INPUT_WIDTH
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

internal object OrtRuntime {
    private val environment: OrtEnvironment = OrtEnvironment.getEnvironment()

    private var modelBuffer: ByteBuffer? = null
    private var options: OrtSession.SessionOptions? = null

    @Synchronized
    fun createSession(modelStream: InputStream): OrtSession {
        val bytes = modelStream.use { it.readBytes() }
        modelBuffer = ByteBuffer.allocateDirect(bytes.size)
            .order(ByteOrder.nativeOrder())
            .apply {
                put(bytes)
                rewind()
            }

        options = OrtSession.SessionOptions().apply {
            setOptimizationLevel(OrtSession.SessionOptions.OptLevel.ALL_OPT)
            setMemoryPatternOptimization(true)

            addCPU(true)

            addConfigEntry("session.load_model_format", "ORT")
            addConfigEntry("session.use_ort_model_bytes_directly", "1")
            addConfigEntry("session.use_ort_model_bytes_for_initializers", "1")
        }

        return environment.createSession(modelBuffer!!, options)
    }

    fun createTensor(data: FloatBuffer): OnnxTensor = OnnxTensor.createTensor(
        environment,
        data,
        longArrayOf(1, 3, MODEL_INPUT_WIDTH.toLong(), MODEL_INPUT_HEIGHT.toLong())
    )
}