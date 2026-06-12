package ru.tanexc.hemoanalysis.tool.analysis.api.domain.results

import ru.tanexc.hemoanalysis.tool.analysis.api.domain.AnalysisException


sealed interface AnalysisResult {
    data class Success(
        val detection: List<Detection>,
        val duration: AnalysisDuration
    ) : AnalysisResult

    data class Error(
        val exception: AnalysisException
    ) : AnalysisResult
}