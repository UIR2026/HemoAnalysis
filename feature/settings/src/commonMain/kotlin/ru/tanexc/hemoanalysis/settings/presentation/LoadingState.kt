package ru.tanexc.hemoanalysis.settings.presentation

sealed interface LoadingState {
    data object Idle: LoadingState

    data class Success(
        val status: ModelStatus
    ): LoadingState

    data class Error(
        val status: ModelStatus
    ): LoadingState

    data object Loading: LoadingState

    data object ApiKeyNotSet: LoadingState
}