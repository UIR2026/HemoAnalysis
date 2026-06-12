package ru.tanexc.hemoanalysis.settings.di

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tanexc.hemoanalysis.domain.State
import ru.tanexc.hemoanalysis.domain.repository.ModelRepository
import ru.tanexc.hemoanalysis.navigation.BaseComponent
import ru.tanexc.hemoanalysis.navigation.Config
import ru.tanexc.hemoanalysis.settings.presentation.LoadingState
import ru.tanexc.hemoanalysis.settings.presentation.ModelStatus
import ru.tanexc.hemoanalysis.settings.presentation.SettingsUiEvent
import ru.tanexc.hemoanalysis.settings.presentation.SettingsUiState

class SettingsComponent(
    componentContext: ComponentContext,
    onNavigate: (Config) -> Unit,
    onBack: () -> Unit,
    private val modelRepository: ModelRepository
) : BaseComponent(componentContext, onNavigate, onBack) {
    private val _state = MutableStateFlow(
        SettingsUiState(
            currentModelInfo = modelRepository.getCurrentModelInfo(),
            latestModelInfo = null,
            loadingState = LoadingState.Idle,
            apiKey = modelRepository.getKeyApi()
        )
    )
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        componentScope.launch(Dispatchers.IO) {
            if (state.value.apiKey.isNotEmpty()) {
                loadModelInfo()
            }
        }
    }

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            SettingsUiEvent.LoadLatestModel -> loadLatestModel()
            SettingsUiEvent.UpdateModelInfo -> updateModelInfo()
            SettingsUiEvent.NavigateBack -> onBack()
            is SettingsUiEvent.UpdateApiKey -> updateApiKey(event.apiKey)
        }
    }

    fun loadLatestModel() {
        componentScope.launch(Dispatchers.IO) {
            modelRepository.loadLatestModel().collect { state ->
                when (state) {
                    is State.Success -> {
                        _state.update {
                            it.copy(
                                loadingState = LoadingState.Success(status = ModelStatus.UpToDate),
                                latestModelInfo = state.data,
                                currentModelInfo = state.data
                            )
                        }
                    }

                    is State.Error -> _state.update { it.copy(loadingState = LoadingState.Error(status = ModelStatus.OutOfDate)) }
                    is State.Loading -> _state.update { it.copy(loadingState = LoadingState.Loading) }
                }
            }
        }
    }

    fun updateModelInfo() {
        componentScope.launch(Dispatchers.IO) {
            modelRepository.updateModelInfo().collect { state ->
                when (state) {
                    is State.Success -> {
                        val latestModelInfo = state.data
                        val currentModelInfo = _state.value.currentModelInfo
                        val status = when {
                            currentModelInfo == null -> ModelStatus.NotPresented
                            !currentModelInfo.isLatest -> ModelStatus.OutOfDate
                            else -> ModelStatus.UpToDate
                        }

                        _state.update {
                            it.copy(
                                loadingState = LoadingState.Success(status = status),
                                latestModelInfo = latestModelInfo,
                                currentModelInfo = modelRepository.getCurrentModelInfo()
                            )
                        }
                    }

                    is State.Error -> _state.update { it.copy(loadingState = LoadingState.Error(status = ModelStatus.UpToDate)) }
                    is State.Loading -> _state.update { it.copy(loadingState = LoadingState.Loading) }
                }
            }
        }
    }

    fun updateApiKey(apiKey: String) {
        componentScope.launch(Dispatchers.IO) {
            modelRepository.updateApiKey(apiKey)
            _state.update { it.copy(apiKey = apiKey) }
            if (apiKey.isNotEmpty()) {
                loadModelInfo()
            }
        }
    }

    fun loadModelInfo() {
        val currentModelInfo = modelRepository.getCurrentModelInfo()
        if (currentModelInfo == null) {
            _state.update { it.copy(loadingState = LoadingState.Success(status = ModelStatus.NotPresented)) }
        } else {
            _state.update {
                it.copy(
                    currentModelInfo = currentModelInfo,
                    loadingState = LoadingState.Success(status = ModelStatus.UpToDate)
                )
            }
        }
        updateModelInfo()
    }
}