package ru.tanexc.hemoanalysis.settings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hemoanalysis.feature.settings.generated.resources.Res
import hemoanalysis.feature.settings.generated.resources.database_off
import hemoanalysis.feature.settings.generated.resources.model_info_no_model
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoModelCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(36.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(22.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                modifier = Modifier.size(56.dp),
                painter = painterResource(Res.drawable.database_off),
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                contentDescription = null,
            )

            Text(
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                text = stringResource(Res.string.model_info_no_model),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )

        }
    }
}