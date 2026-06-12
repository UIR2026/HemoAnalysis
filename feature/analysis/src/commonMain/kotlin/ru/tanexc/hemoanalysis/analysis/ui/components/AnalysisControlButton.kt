package ru.tanexc.hemoanalysis.analysis.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hemoanalysis.feature.analysis.generated.resources.Res
import hemoanalysis.feature.analysis.generated.resources.cancel
import hemoanalysis.feature.analysis.generated.resources.select_image
import hemoanalysis.feature.analysis.generated.resources.start_analysis
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AnalysisControlButton(
    modifier: Modifier = Modifier,
    onSelectImage: () -> Unit,
    onCancelImage: () -> Unit,
    onStartAnalysis: () -> Unit,
    isImageSelected: Boolean,
    isAnalysisInProgress: Boolean
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Button(
            modifier = Modifier.weight(1f),
            shape = if (isImageSelected) {
                RoundedCornerShape(50, 5, 5, 50)
            } else {
                CircleShape
            },
            enabled = !isAnalysisInProgress,
            onClick = {
                if (!isImageSelected) {
                    onSelectImage()
                } else {
                    onStartAnalysis()
                }
            }
        ) {
            val text = if (!isImageSelected) {
                stringResource(Res.string.select_image)
            } else {
                stringResource(Res.string.start_analysis)
            }

            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = text
            )
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxHeight(),
            visible = isImageSelected,
            enter = slideInHorizontally() + expandHorizontally()
        ) {
            Button(
                modifier = Modifier.fillMaxHeight(),
                shape = RoundedCornerShape(5, 50, 50, 5),
                enabled = !isAnalysisInProgress,
                onClick = onCancelImage
            ) {
                Icon(
                    painter = painterResource(Res.drawable.cancel),
                    contentDescription = null
                )
            }
        }
    }
}