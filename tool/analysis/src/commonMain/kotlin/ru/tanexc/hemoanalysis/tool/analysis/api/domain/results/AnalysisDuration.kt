package ru.tanexc.hemoanalysis.tool.analysis.api.domain.results

import kotlin.time.Duration

data class AnalysisDuration(
    val preprocessing: Duration = Duration.ZERO,
    val inference: Duration = Duration.ZERO,
    val postprocessing: Duration = Duration.ZERO,
) {
    val total: Duration = preprocessing + inference + postprocessing
}