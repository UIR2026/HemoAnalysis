package ru.tanexc.hemoanalysis.analysis.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import hemoanalysis.feature.analysis.generated.resources.Res
import hemoanalysis.feature.analysis.generated.resources.analysis
import hemoanalysis.feature.analysis.generated.resources.parameters
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.tanexc.hemoanalysis.analysis.di.AnalysisComponent
import ru.tanexc.hemoanalysis.analysis.presentation.AnalysisUiEvent
import ru.tanexc.hemoanalysis.analysis.presentation.Status
import ru.tanexc.hemoanalysis.analysis.presentation.UpdateParametersUiEvent
import ru.tanexc.hemoanalysis.analysis.ui.components.AnalysisControlButton
import ru.tanexc.hemoanalysis.analysis.ui.components.AnalysisParametersSheet
import ru.tanexc.hemoanalysis.analysis.ui.components.Picture
import ru.tanexc.hemoanalysis.analysis.ui.components.errorBlock
import ru.tanexc.hemoanalysis.analysis.ui.components.reportBlock
import ru.tanexc.hemoanalysis.analysis.util.round

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnalysisScreen(
    paddingValues: PaddingValues,
    component: AnalysisComponent
) {
    val state by component
        .state
        .collectAsState()

    Column(
        modifier = Modifier.padding(
            top = 0.dp,
            bottom = paddingValues.calculateBottomPadding()
        ).fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(Res.string.analysis)) },
            actions = {
                IconButton(
                    onClick = { component.onEvent(AnalysisUiEvent.OpenParameters) },
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.parameters),
                        contentDescription = null
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            overscrollEffect = null,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(0.dp, 0.dp, 36.dp, 36.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(12.dp),
                ) {
                    Picture(
                        modifier = Modifier.fillMaxWidth(),
                        imageBitmap = state.imageBitmap,
                        status = state.status,
                        onSelectImage = { component.onEvent(AnalysisUiEvent.SelectImage) }
                    )
                    Spacer(Modifier.size(12.dp))
                    AnalysisControlButton(
                        onSelectImage = { component.onEvent(AnalysisUiEvent.SelectImage) },
                        onCancelImage = { component.onEvent(AnalysisUiEvent.CancelImage) },
                        onStartAnalysis = { component.onEvent(AnalysisUiEvent.StartAnalysis) },
                        isImageSelected = state.imageBitmap != null,
                        isAnalysisInProgress = state.status == Status.Loading
                    )

                }
            }

            when (val status = state.status) {
                is Status.Success -> reportBlock(
                    detections = status.detections,
                    duration = status.duration
                )

                is Status.Failed -> errorBlock(
                    status.exception
                )

                else -> {}
            }
        }
    }


    AnalysisParametersSheet(
        isVisible = state.showParameters,
        confidenceThreshold = state.confidenceThreshold,
        iouThreshold = state.iouThreshold,
        onDismiss = { component.onEvent(AnalysisUiEvent.CloseParameters) },
        onConfThresholdChange = {
            component.onEvent(
                UpdateParametersUiEvent.ChangeConfidenceThreshold(
                    it.round(2)
                )
            )
        },
        onIouThresholdChange = {
            component.onEvent(
                UpdateParametersUiEvent.ChangeIouThreshold(
                    it.round(2)
                )
            )
        },
        preprocessVariant = state.preprocessVariant,
        roiVariant = state.roiVariant,
        onPreprocessVariantChange = {
            component.onEvent(
                UpdateParametersUiEvent.ChangePreprocessVariant(it)
            )
        },
        onRoiVariantChange = {
            component.onEvent(
                UpdateParametersUiEvent.ChangeRoiVariant(it)
            )
        },
    )
}
