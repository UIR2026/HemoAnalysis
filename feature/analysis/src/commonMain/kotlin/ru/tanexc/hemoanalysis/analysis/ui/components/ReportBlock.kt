package ru.tanexc.hemoanalysis.analysis.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hemoanalysis.feature.analysis.generated.resources.Res
import hemoanalysis.feature.analysis.generated.resources.report_inference_duration
import hemoanalysis.feature.analysis.generated.resources.report_postprocessing_duration
import hemoanalysis.feature.analysis.generated.resources.report_preprocessing_duration
import hemoanalysis.feature.analysis.generated.resources.report_title
import hemoanalysis.feature.analysis.generated.resources.report_total_duration
import org.jetbrains.compose.resources.stringResource
import ru.tanexc.hemoanalysis.analysis.ui.title
import ru.tanexc.hemoanalysis.analysis.util.round
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.results.AnalysisDuration
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.results.Detection
import kotlin.time.DurationUnit

fun LazyListScope.reportBlock(
    detections: List<Detection>,
    duration: AnalysisDuration
) {
    item {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .clip(RoundedCornerShape(36.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(Res.string.report_preprocessing_duration),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = duration.preprocessing.toString(
                            unit = DurationUnit.MILLISECONDS,
                            decimals = 1
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(Res.string.report_inference_duration),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = duration.inference.toString(
                            unit = DurationUnit.MILLISECONDS,
                            decimals = 1
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(Res.string.report_postprocessing_duration),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = duration.postprocessing.toString(
                            unit = DurationUnit.MILLISECONDS,
                            decimals = 1
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(Res.string.report_total_duration),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = duration.total.toString(
                            unit = DurationUnit.MILLISECONDS,
                            decimals = 1
                        )
                    )
                }
            }
        }
    }

    if (detections.isNotEmpty()) {
        item {
            Text(
                modifier = Modifier,
                text = stringResource(Res.string.report_title),
                fontWeight = FontWeight.Black,
                fontSize = 24.sp
            )
            Spacer(Modifier.size(8.dp))
        }
    }

    detections
        .groupBy { it.cellClass }
        .forEach { (cell, detects) ->
            stickyHeader {
                CellHeader(cell, detections.size)
            }
            items(detects) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(it.cellClass.title())
                    Text(it.confidence.round(2).toString())
                }
            }
        }

}