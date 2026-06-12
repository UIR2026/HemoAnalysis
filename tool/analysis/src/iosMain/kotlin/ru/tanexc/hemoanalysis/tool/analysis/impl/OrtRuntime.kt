@file:OptIn(ExperimentalForeignApi::class)

package ru.tanexc.hemoanalysis.tool.analysis.impl

import cocoapods.onnxruntime_objc.ORTEnv
import cocoapods.onnxruntime_objc.ORTGraphOptimizationLevel
import cocoapods.onnxruntime_objc.ORTLoggingLevel
import cocoapods.onnxruntime_objc.ORTSession
import cocoapods.onnxruntime_objc.ORTSessionOptions
import cocoapods.onnxruntime_objc.ORTTensorElementDataType
import cocoapods.onnxruntime_objc.ORTValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.Foundation.NSMutableData
import platform.Foundation.NSNumber
import platform.Foundation.dataWithLength
import platform.Foundation.numberWithLongLong
import platform.posix.memcpy
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.MODEL_INPUT_HEIGHT
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.MODEL_INPUT_WIDTH

internal object OrtRuntime {
    private val environment: ORTEnv = handleThrowingError { error ->
        ORTEnv(
            loggingLevel = ORTLoggingLevel.ORTLoggingLevelWarning,
            error = error
        )
    }

    private val options: ORTSessionOptions = handleThrowingError { error ->
        ORTSessionOptions(error = error).apply {
            setGraphOptimizationLevel(
                ORTGraphOptimizationLevel.ORTGraphOptimizationLevelAll,
                error = error
            )
            setGraphOptimizationLevel(
                ORTGraphOptimizationLevel.ORTGraphOptimizationLevelAll,
                error = error
            )
            addConfigEntryWithKey(
                "session.load_model_format",
                value = "ORT",
                error = error
            )
        }
    }

    fun createSession(modelPath: String): ORTSession =
        handleThrowingError { error ->
            ORTSession(
                env = environment,
                modelPath = modelPath,
                sessionOptions = options,
                error = error
            )
        }

    fun createTensor(tensorData: FloatArray): ORTValue =
        handleThrowingError { error ->
            ORTValue(
                tensorData = tensorData.toNSMutableData(),
                elementType = ORTTensorElementDataType.ORTTensorElementDataTypeFloat,
                shape = listOf(
                    NSNumber.numberWithLongLong(1),
                    NSNumber.numberWithLongLong(3),
                    NSNumber.numberWithLongLong(MODEL_INPUT_WIDTH.toLong()),
                    NSNumber.numberWithLongLong(MODEL_INPUT_HEIGHT.toLong())
                ),
                error = error
            )
        }

    private fun FloatArray.toNSMutableData(): NSMutableData {
        val byteSize = size * Float.SIZE_BYTES

        val data = NSMutableData.dataWithLength(byteSize.convert())
            ?: error("Failed to allocate input tensor data: $byteSize bytes")

        usePinned { pinned ->
            memcpy(
                data.mutableBytes,
                pinned.addressOf(0),
                byteSize.convert()
            )
        }

        return data
    }
}