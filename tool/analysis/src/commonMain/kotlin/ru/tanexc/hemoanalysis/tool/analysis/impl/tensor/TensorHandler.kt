package ru.tanexc.hemoanalysis.tool.analysis.impl.tensor

import ru.tanexc.hemoanalysis.tool.analysis.impl.core.blue
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.green
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.red
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.ImageBuffer

internal class TensorHandler {
    operator fun invoke(imageBuffer: ImageBuffer): FloatArray = with(imageBuffer) {
        val channelSize = width * height
        val tensor = FloatArray(3 * channelSize)

        for (i in pixels.indices) {
            val pixel = pixels[i]

            tensor[i] = red(pixel) / 255f
            tensor[channelSize + i] = green(pixel) / 255f
            tensor[channelSize * 2 + i] = blue(pixel) / 255f
        }

        return tensor
    }
}
