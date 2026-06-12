package ru.tanexc.hemoanalysis.tool.analysis.api

import ru.tanexc.hemoanalysis.tool.analysis.api.domain.parameters.AnalysisParameters
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.results.AnalysisResult

interface AnalysisTool {
    fun performAnalysis(parameters: AnalysisParameters): AnalysisResult
}