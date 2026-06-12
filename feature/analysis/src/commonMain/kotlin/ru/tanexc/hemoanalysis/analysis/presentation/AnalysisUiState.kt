package ru.tanexc.hemoanalysis.analysis.presentation

import androidx.compose.ui.graphics.ImageBitmap
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.parameters.PreprocessVariant
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.parameters.RoiVariant

data class AnalysisUiState(
    val status: Status = Status.Idle,
    val imageBitmap: ImageBitmap? = null,
    val showParameters: Boolean = false,
    val confidenceThreshold: Float = 0.5f,
    val iouThreshold: Float = 0.5f,
    val preprocessVariant: PreprocessVariant = PreprocessVariant.Default,
    val roiVariant: RoiVariant = RoiVariant.None
)