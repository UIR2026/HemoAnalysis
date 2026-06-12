package ru.tanexc.hemoanalysis.analysis.di

import com.arkivanov.decompose.ComponentContext
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.util.toImageBitmap
import io.github.vinceglb.filekit.dialogs.openFilePicker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.tanexc.hemoanalysis.analysis.presentation.AnalysisUiEvent
import ru.tanexc.hemoanalysis.analysis.presentation.AnalysisUiState
import ru.tanexc.hemoanalysis.analysis.presentation.Status
import ru.tanexc.hemoanalysis.analysis.presentation.UpdateParametersUiEvent
import ru.tanexc.hemoanalysis.analysis.util.imageBuffer
import ru.tanexc.hemoanalysis.navigation.BaseComponent
import ru.tanexc.hemoanalysis.navigation.Config
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.parameters.AnalysisParameters
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.results.AnalysisResult
import ru.tanexc.hemoanalysis.tool.analysis.api.AnalysisTool
import ru.tanexc.hemoanalysis.domain.repository.AnalysisParamsRepository
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.parameters.PreprocessVariant
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.parameters.RoiVariant

class AnalysisComponent(
    componentContext: ComponentContext,
    onNavigate: (Config) -> Unit,
    onBack: () -> Unit,
    private val analysisTool: AnalysisTool,
    private val analysisParamsRepositoryImpl: AnalysisParamsRepository
) : BaseComponent(componentContext, onNavigate, onBack) {
    private val _state = MutableStateFlow(
        AnalysisUiState(
            confidenceThreshold = analysisParamsRepositoryImpl.confThreshold,
            iouThreshold = analysisParamsRepositoryImpl.iouThreshold,
            preprocessVariant = PreprocessVariant.entries[analysisParamsRepositoryImpl.preprocessVariant],
            roiVariant = RoiVariant.entries[analysisParamsRepositoryImpl.roiVariant]
        )
    )
    val state: StateFlow<AnalysisUiState> = _state.asStateFlow()

    fun onEvent(event: AnalysisUiEvent) {
        when (event) {
            is AnalysisUiEvent.SelectImage -> selectImage()
            is AnalysisUiEvent.StartAnalysis -> startAnalysis()
            is AnalysisUiEvent.CancelImage -> cancelImage()
            is UpdateParametersUiEvent.ChangeConfidenceThreshold -> {
                _state.update { it.copy(confidenceThreshold = event.value) }
                analysisParamsRepositoryImpl.confThreshold = event.value
            }

            is UpdateParametersUiEvent.ChangeIouThreshold -> {
                _state.update { it.copy(iouThreshold = event.value) }
                analysisParamsRepositoryImpl.iouThreshold = event.value
            }

            is AnalysisUiEvent.CloseParameters -> _state.update { it.copy(showParameters = false) }
            is AnalysisUiEvent.OpenParameters -> _state.update { it.copy(showParameters = true) }
            is UpdateParametersUiEvent.ChangePreprocessVariant -> _state.update {
                analysisParamsRepositoryImpl.preprocessVariant = event.value.ordinal
                it.copy(preprocessVariant = event.value)
            }

            is UpdateParametersUiEvent.ChangeRoiVariant -> _state.update {
                analysisParamsRepositoryImpl.roiVariant = event.value.ordinal
                it.copy(roiVariant = event.value)
            }

        }
    }

    private fun selectImage() {
        componentScope.launch {
            FileKit.openFilePicker(FileKitType.Image)?.let { imageFile ->
                _state.update {
                    it.copy(imageBitmap = imageFile.toImageBitmap())
                }
            }
        }
    }

    private fun startAnalysis() {
        componentScope.launch {
            _state.update { it.copy(status = Status.Loading) }
            val bitmap = state.value.imageBitmap ?: return@launch
            val params = AnalysisParameters(
                imageBuffer = bitmap.imageBuffer,
                confThreshold = state.value.confidenceThreshold,
                iouThreshold = state.value.iouThreshold,
                preprocessVariant = state.value.preprocessVariant,
                roiVariant = state.value.roiVariant
            )
            val result = analysisTool.performAnalysis(params)

            when (result) {
                is AnalysisResult.Error -> _state.update {
                    it.copy(status = Status.Failed(exception = result.exception))
                }

                is AnalysisResult.Success -> _state.update {
                    it.copy(
                        status = Status.Success(
                            detections = result.detection,
                            duration = result.duration
                        ),
                    )
                }
            }
        }
    }

    private fun cancelImage() {
        _state.update {
            it.copy(
                status = Status.Idle,
                imageBitmap = null
            )
        }
    }
}
