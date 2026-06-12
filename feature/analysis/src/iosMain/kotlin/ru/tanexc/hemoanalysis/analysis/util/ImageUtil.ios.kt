package ru.tanexc.hemoanalysis.analysis.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.ImageBuffer

internal actual val ImageBuffer.imageBitmap: ImageBitmap
    get() {
        val bytes = ByteArray(width * height * 4)

        var byteIndex = 0

        for (pixel in pixels) {
            val a = pixel ushr 24 and 0xFF
            val r = pixel ushr 16 and 0xFF
            val g = pixel ushr 8 and 0xFF
            val b = pixel and 0xFF

            bytes[byteIndex++] = b.toByte()
            bytes[byteIndex++] = g.toByte()
            bytes[byteIndex++] = r.toByte()
            bytes[byteIndex++] = a.toByte()
        }

        val imageInfo = ImageInfo(
            width = width,
            height = height,
            colorType = ColorType.BGRA_8888,
            alphaType = ColorAlphaType.UNPREMUL
        )

        return Image.makeRaster(
            imageInfo = imageInfo,
            bytes = bytes,
            rowBytes = width * 4
        ).toComposeImageBitmap()
    }