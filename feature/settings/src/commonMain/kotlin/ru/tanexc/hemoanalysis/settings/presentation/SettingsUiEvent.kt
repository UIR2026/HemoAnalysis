package ru.tanexc.hemoanalysis.settings.presentation

sealed interface SettingsUiEvent {
    data object UpdateModelInfo: SettingsUiEvent

    data object LoadLatestModel: SettingsUiEvent

    data object NavigateBack: SettingsUiEvent

    data class UpdateApiKey(
        val apiKey: String
    ): SettingsUiEvent
}