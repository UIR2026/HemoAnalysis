package ru.tanexc.hemoanalysis.analysis.presentation

import ru.tanexc.hemoanalysis.tool.analysis.api.domain.parameters.PreprocessVariant
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.parameters.RoiVariant

sealed interface AnalysisUiEvent {
    data object SelectImage: AnalysisUiEvent

    data object StartAnalysis: AnalysisUiEvent

    data object CancelImage: AnalysisUiEvent

    data object OpenParameters: AnalysisUiEvent

    data object CloseParameters: AnalysisUiEvent
}

sealed interface UpdateParametersUiEvent: AnalysisUiEvent {
    data class ChangeConfidenceThreshold(
        val value: Float
    ): UpdateParametersUiEvent

    data class ChangeIouThreshold(
        val value: Float
    ): UpdateParametersUiEvent

    data class ChangePreprocessVariant(
        val value: PreprocessVariant
    ): UpdateParametersUiEvent

    data class ChangeRoiVariant(
        val value: RoiVariant
    ): UpdateParametersUiEvent
}