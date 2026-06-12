package ru.tanexc.hemoanalysis.settings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hemoanalysis.feature.settings.generated.resources.Res
import hemoanalysis.feature.settings.generated.resources.model_info_created_at
import hemoanalysis.feature.settings.generated.resources.model_info_last_update
import hemoanalysis.feature.settings.generated.resources.model_info_size_content
import hemoanalysis.feature.settings.generated.resources.model_info_size_title
import hemoanalysis.feature.settings.generated.resources.model_info_title
import hemoanalysis.feature.settings.generated.resources.model_info_version
import org.jetbrains.compose.resources.stringResource
import ru.tanexc.hemoanalysis.domain.model.Mb
import ru.tanexc.hemoanalysis.domain.model.Size
import kotlin.math.round

@Composable
fun ModelInfoCard(
    version: String,
    size: Size,
    createdAtTime: String,
    lastUpdateTime: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(36.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(22.dp)
                .fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.model_info_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.size(22.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.model_info_version),
                    fontWeight = FontWeight.Bold
                )
                Text(text = version)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.model_info_size_title),
                    fontWeight = FontWeight.Bold
                )
                Text(text = "${size.formattedMb()} " + stringResource(Res.string.model_info_size_content))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.model_info_created_at),
                    fontWeight = FontWeight.Bold
                )
                Text(text = createdAtTime)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.model_info_last_update),
                    fontWeight = FontWeight.Bold
                )
                Text(text = lastUpdateTime)
            }
        }
    }
}

private fun Size.formattedMb(): String {
    val rounded = round(Mb * 10) / 10
    return rounded.toString()
}
