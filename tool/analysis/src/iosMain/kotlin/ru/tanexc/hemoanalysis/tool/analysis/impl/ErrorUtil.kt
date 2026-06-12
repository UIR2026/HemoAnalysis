package ru.tanexc.hemoanalysis.tool.analysis.impl

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSError


@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class)
internal inline fun <T> handleThrowingError(
    block: (CPointer<ObjCObjectVar<NSError?>>) -> T?
): T = memScoped {
    val error = alloc<ObjCObjectVar<NSError?>>()
    error.value = null

    val result = block(error.ptr)
    result ?: throw IllegalStateException(
        "ONNX Runtime failed: ${error.value?.localizedDescription}"
    )
}