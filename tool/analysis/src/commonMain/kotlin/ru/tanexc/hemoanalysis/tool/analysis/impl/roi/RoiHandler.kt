package ru.tanexc.hemoanalysis.tool.analysis.impl.roi

import ru.tanexc.hemoanalysis.tool.analysis.api.domain.ImageBuffer
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.parameters.RoiVariant

internal class RoiHandler {
    operator fun invoke(
        imageBuffer: ImageBuffer,
        roiVariant: RoiVariant
    ): RoiOutput {
        val image = when (roiVariant) {
            RoiVariant.ThresholdMorph -> thresholdMorph(imageBuffer)
            RoiVariant.ThresholdFilter -> thresholdFilter(imageBuffer)
            RoiVariant.None -> imageBuffer
        }
        return RoiOutput(image)
    }
}
