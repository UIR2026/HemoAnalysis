package ru.tanexc.hemoanalysis.tool.analysis.api.domain.parameters

import ru.tanexc.hemoanalysis.tool.analysis.api.domain.ImageBuffer

data class AnalysisParameters(
    val imageBuffer: ImageBuffer,
    val confThreshold: Float,
    val iouThreshold: Float,
    val preprocessVariant: PreprocessVariant,
    val roiVariant: RoiVariant
)