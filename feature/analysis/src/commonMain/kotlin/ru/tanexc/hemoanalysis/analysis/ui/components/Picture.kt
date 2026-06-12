package ru.tanexc.hemoanalysis.analysis.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import hemoanalysis.feature.analysis.generated.resources.Res
import hemoanalysis.feature.analysis.generated.resources.analysis
import org.jetbrains.compose.resources.painterResource
import ru.tanexc.hemoanalysis.analysis.presentation.Status
import ru.tanexc.hemoanalysis.analysis.ui.strokeColor

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Picture(
    modifier: Modifier = Modifier,
    status: Status,
    imageBitmap: ImageBitmap? = null,
    onSelectImage: () -> Unit
) {
    val isLoading = status == Status.Loading
    val detections by remember(status) {
        derivedStateOf {
            when (status) {
                is Status.Success -> status.detections
                else -> emptyList()
            }
        }
    }

    imageBitmap?.let {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .clip(RoundedCornerShape(36.dp))
                    .fillMaxWidth()
                    .aspectRatio(imageBitmap.width.toFloat() / imageBitmap.height)
            ) {
                val height = size.height
                val width = size.width
                val scaleX = width / imageBitmap.width
                val scaleY = height / imageBitmap.height

                drawImage(
                    image = imageBitmap,
                    srcSize = IntSize(imageBitmap.width, imageBitmap.height),
                    dstSize = IntSize(width.toInt(), height.toInt()),
                    alpha = if (isLoading) 0.3f else 1f
                )

                detections.forEach { detection ->
                    val left = (detection.centerX - detection.width / 2f) * scaleX
                    val top = (detection.centerY - detection.height / 2f) * scaleY
                    val boxWidth = detection.width * scaleX
                    val boxHeight = detection.height * scaleY

                    drawRoundRect(
                        color = detection.cellClass.strokeColor(),
                        topLeft = Offset(left, top),
                        size = Size(boxWidth, boxHeight),
                        cornerRadius = CornerRadius(12f, 12f),
                        style = Stroke(
                            width = 8f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
                        )
                    )
                }
            }

            if (isLoading) {
                CircularWavyProgressIndicator()
            }
        }

    } ?: Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
            .clip(RoundedCornerShape(36.dp))
            .clickable(onClick = onSelectImage),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val shape = RoundedPolygon.star(
                numVerticesPerRadius = 4,
                innerRadius = 0.4f,
                rounding = CornerRounding(0.5f, 0.2f)
            ).normalized().toShape(45)

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.secondary, shape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(0.5f),
                    painter = painterResource(Res.drawable.analysis),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}