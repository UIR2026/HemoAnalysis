package ru.tanexc.hemoanalysis.settings.presentation

import ru.tanexc.hemoanalysis.domain.model.ModelInfo

data class SettingsUiState(
    val currentModelInfo: ModelInfo?,
    val latestModelInfo: ModelInfo?,
    val loadingState: LoadingState,
    val apiKey: String
)