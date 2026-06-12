package ru.tanexc.hemoanalysis.analysis.presentation

import ru.tanexc.hemoanalysis.tool.analysis.api.domain.AnalysisException
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.results.AnalysisDuration
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.results.Detection

sealed interface Status {
    data object Idle: Status

    data object Loading: Status

    data class Success(
        val detections: List<Detection>,
        val duration: AnalysisDuration
    ): Status

    data class Failed(
        val exception: AnalysisException
    ): Status
}