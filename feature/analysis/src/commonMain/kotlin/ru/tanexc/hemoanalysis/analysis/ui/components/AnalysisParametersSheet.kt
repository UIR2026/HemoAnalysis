package ru.tanexc.hemoanalysis.analysis.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hemoanalysis.feature.analysis.generated.resources.Res
import hemoanalysis.feature.analysis.generated.resources.confidence
import hemoanalysis.feature.analysis.generated.resources.iou
import hemoanalysis.feature.analysis.generated.resources.parameters_title
import hemoanalysis.feature.analysis.generated.resources.preprocess_params_title
import hemoanalysis.feature.analysis.generated.resources.preprocess_variant_brightness_contrast
import hemoanalysis.feature.analysis.generated.resources.preprocess_variant_deafult
import hemoanalysis.feature.analysis.generated.resources.roi_params_title
import hemoanalysis.feature.analysis.generated.resources.roi_variant_morph
import hemoanalysis.feature.analysis.generated.resources.roi_variant_none
import hemoanalysis.feature.analysis.generated.resources.roi_variant_threshold_filter
import org.jetbrains.compose.resources.stringResource
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.parameters.PreprocessVariant
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.parameters.RoiVariant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnalysisParametersSheet(
    isVisible: Boolean,
    confidenceThreshold: Float,
    iouThreshold: Float,
    preprocessVariant: PreprocessVariant,
    roiVariant: RoiVariant,
    onDismiss: () -> Unit,
    onConfThresholdChange: (Float) -> Unit,
    onIouThresholdChange: (Float) -> Unit,
    onPreprocessVariantChange: (PreprocessVariant) -> Unit,
    onRoiVariantChange: (RoiVariant) -> Unit

) {
    if (isVisible) {
        ModalBottomSheet(
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            onDismissRequest = onDismiss
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.parameters_title),
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )

            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                TitledSlider(
                    value = confidenceThreshold,
                    title = stringResource(Res.string.confidence),
                    onValueChange = onConfThresholdChange
                )

                TitledSlider(
                    value = iouThreshold,
                    title = stringResource(Res.string.iou),
                    onValueChange = onIouThresholdChange
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.preprocess_params_title),
                        fontWeight = FontWeight.Bold
                    )
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
                    ) {
                        SegmentedButton(
                            modifier = Modifier.fillMaxHeight(),
                            selected = preprocessVariant == PreprocessVariant.Default,
                            onClick = { onPreprocessVariantChange(PreprocessVariant.Default) },
                            shape = SegmentedButtonDefaults.itemShape(0, 2),
                            label = {
                                Text(
                                    text = stringResource(Res.string.preprocess_variant_deafult),
                                    textAlign = TextAlign.Center
                                )
                            },
                            icon = {}
                        )
                        SegmentedButton(
                            modifier = Modifier.fillMaxHeight(),
                            selected = preprocessVariant == PreprocessVariant.BrightnessContrast,
                            onClick = { onPreprocessVariantChange(PreprocessVariant.BrightnessContrast) },
                            shape = SegmentedButtonDefaults.itemShape(1, 2),
                            label = {
                                Text(
                                    text = stringResource(Res.string.preprocess_variant_brightness_contrast),
                                    textAlign = TextAlign.Center
                                )
                            },
                            icon = {}
                        )
                    }
                }


                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.roi_params_title),
                        fontWeight = FontWeight.Bold
                    )
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
                    ) {
                        SegmentedButton(
                            modifier = Modifier.fillMaxHeight(),
                            selected = roiVariant == RoiVariant.None,
                            onClick = { onRoiVariantChange(RoiVariant.None) },
                            shape = SegmentedButtonDefaults.itemShape(0, 3),
                            label = {
                                Text(
                                    text = stringResource(Res.string.roi_variant_none),
                                    textAlign = TextAlign.Center
                                )
                            },
                            icon = {}
                        )
                        SegmentedButton(
                            modifier = Modifier.fillMaxHeight(),
                            selected = roiVariant == RoiVariant.ThresholdMorph,
                            onClick = { onRoiVariantChange(RoiVariant.ThresholdMorph) },
                            shape = SegmentedButtonDefaults.itemShape(1, 3),
                            label = {
                                Text(
                                    text = stringResource(Res.string.roi_variant_morph),
                                    textAlign = TextAlign.Center
                                )
                            },
                            icon = {}
                        )
                        SegmentedButton(
                            modifier = Modifier.fillMaxHeight(),
                            selected = roiVariant == RoiVariant.ThresholdFilter,
                            onClick = { onRoiVariantChange(RoiVariant.ThresholdFilter) },
                            shape = SegmentedButtonDefaults.itemShape(2, 3),
                            label = {
                                Text(
                                    text = stringResource(Res.string.roi_variant_threshold_filter),
                                    textAlign = TextAlign.Center
                                )
                            },
                            icon = {}
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TitledSlider(
    value: Float,
    title: String,
    onValueChange: (Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
            Text(value.toString())
        }

        Slider(
            value = value,
            modifier = Modifier.fillMaxWidth(),
            steps = 100,
            valueRange = 0f..1f,
            colors = SliderDefaults.colors().copy(
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            ),
            onValueChange = onValueChange,
        )
    }
}



