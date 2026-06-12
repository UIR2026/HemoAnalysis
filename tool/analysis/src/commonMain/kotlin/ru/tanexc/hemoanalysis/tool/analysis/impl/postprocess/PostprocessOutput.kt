package ru.tanexc.hemoanalysis.tool.analysis.impl.postprocess

import ru.tanexc.hemoanalysis.tool.analysis.api.domain.results.Detection

internal data class PostprocessOutput(
    val detections: List<Detection>
)