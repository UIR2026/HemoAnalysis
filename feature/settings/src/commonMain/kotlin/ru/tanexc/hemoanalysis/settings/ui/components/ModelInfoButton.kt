package ru.tanexc.hemoanalysis.settings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import hemoanalysis.feature.settings.generated.resources.Res
import hemoanalysis.feature.settings.generated.resources.download
import hemoanalysis.feature.settings.generated.resources.model_info_check_for_updates
import hemoanalysis.feature.settings.generated.resources.model_info_download_model
import hemoanalysis.feature.settings.generated.resources.model_info_error
import hemoanalysis.feature.settings.generated.resources.model_info_update_available
import hemoanalysis.feature.settings.generated.resources.restart
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.tanexc.hemoanalysis.settings.presentation.ModelStatus

@Composable
fun ModelInfoButton(
    status: ModelStatus,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = CircleShape,
        enabled = isEnabled,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
        val (text, icon) = when (status) {
            ModelStatus.NotPresented -> stringResource(Res.string.model_info_download_model) to painterResource(
                Res.drawable.download
            )

            ModelStatus.OutOfDate -> stringResource(Res.string.model_info_update_available) to painterResource(
                Res.drawable.download
            )

            ModelStatus.UpToDate -> stringResource(Res.string.model_info_check_for_updates) to painterResource(
                Res.drawable.restart
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}