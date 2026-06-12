package ru.tanexc.hemoanalysis.tool.analysis.impl.roi

import ru.tanexc.hemoanalysis.tool.analysis.api.domain.ImageBuffer
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.BACKGROUND_COLOR
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.close
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.gray
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.open
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.otsuThreshold

internal fun thresholdMorph(imageBuffer: ImageBuffer): ImageBuffer {
    val threshold = otsuThreshold(imageBuffer)
    val mask = BooleanArray(imageBuffer.pixels.size) {
        gray(imageBuffer.pixels[it]) <= threshold
    }
    val morphedMask = close(
        open(mask, imageBuffer.width, imageBuffer.height),
        imageBuffer.width,
        imageBuffer.height
    )
    val result = IntArray(imageBuffer.pixels.size)

    for (i in imageBuffer.pixels.indices) {
        val pixel = imageBuffer.pixels[i]
        result[i] = if (morphedMask[i]) pixel else BACKGROUND_COLOR
    }

    return imageBuffer.copy(pixels = result)
}