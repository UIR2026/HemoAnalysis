package ru.tanexc.hemoanalysis.tool.analysis.impl.roi

import ru.tanexc.hemoanalysis.tool.analysis.api.domain.ImageBuffer
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.BACKGROUND_COLOR
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.gray
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.otsuThreshold


internal fun thresholdFilter(imageBuffer: ImageBuffer): ImageBuffer {
    val threshold = otsuThreshold(imageBuffer)
    val result = IntArray(imageBuffer.pixels.size)

    for (i in imageBuffer.pixels.indices) {
        val pixel = imageBuffer.pixels[i]
        result[i] = if (gray(pixel) <= threshold) pixel else BACKGROUND_COLOR
    }

    return imageBuffer.copy(pixels = result)
}
