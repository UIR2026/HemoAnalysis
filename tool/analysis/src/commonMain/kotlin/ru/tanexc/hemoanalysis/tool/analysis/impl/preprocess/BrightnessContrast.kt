package ru.tanexc.hemoanalysis.tool.analysis.impl.preprocess

import ru.tanexc.hemoanalysis.tool.analysis.api.domain.ImageBuffer
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.gray
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.rgb

fun brightnessContrast(imageBuffer: ImageBuffer): ImageBuffer {
    val luminance = IntArray(imageBuffer.pixels.size) { gray(imageBuffer.pixels[it]) }
    val min = luminance.minOrNull() ?: return imageBuffer
    val max = luminance.maxOrNull() ?: return imageBuffer
    if (max == min) return imageBuffer

    val result = IntArray(imageBuffer.pixels.size)
    for (i in imageBuffer.pixels.indices) {
        val pixel = imageBuffer.pixels[i]
        val oldLum = luminance[i].coerceAtLeast(1)
        val newLum = ((luminance[i] - min) * 255 / (max - min)).coerceIn(0, 255)
        val scale = newLum.toDouble() / oldLum

        result[i] = rgb(
            ((pixel ushr 16 and 0xFF) * scale).toInt().coerceIn(0, 255),
            ((pixel ushr 8 and 0xFF) * scale).toInt().coerceIn(0, 255),
            ((pixel and 0xFF) * scale).toInt().coerceIn(0, 255)
        )
    }

    return imageBuffer.copy(pixels = result)
}