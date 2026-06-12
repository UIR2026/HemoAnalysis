package ru.tanexc.hemoanalysis.tool.analysis.api.domain.results

import ru.tanexc.hemoanalysis.tool.analysis.api.domain.results.CellClass

data class Detection(
    val cellClass: CellClass,
    val confidence: Float,
    val centerX: Float,
    val centerY: Float,
    val width: Float,
    val height: Float,
)
