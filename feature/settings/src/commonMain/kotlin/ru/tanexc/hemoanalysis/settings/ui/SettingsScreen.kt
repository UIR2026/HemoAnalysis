package ru.tanexc.hemoanalysis.settings.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hemoanalysis.feature.settings.generated.resources.Res
import hemoanalysis.feature.settings.generated.resources.api_key_label
import hemoanalysis.feature.settings.generated.resources.cancel
import hemoanalysis.feature.settings.generated.resources.model_info_created_at
import hemoanalysis.feature.settings.generated.resources.model_info_last_update
import hemoanalysis.feature.settings.generated.resources.model_info_no_model
import hemoanalysis.feature.settings.generated.resources.model_info_title
import hemoanalysis.feature.settings.generated.resources.model_info_version
import hemoanalysis.feature.settings.generated.resources.settings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.tanexc.hemoanalysis.domain.model.ModelInfo
import ru.tanexc.hemoanalysis.settings.di.SettingsComponent
import ru.tanexc.hemoanalysis.settings.presentation.LoadingState
import ru.tanexc.hemoanalysis.settings.presentation.ModelStatus
import ru.tanexc.hemoanalysis.settings.presentation.SettingsUiEvent
import ru.tanexc.hemoanalysis.settings.ui.components.ErrorCard
import ru.tanexc.hemoanalysis.settings.ui.components.ModelInfoButton
import ru.tanexc.hemoanalysis.settings.ui.components.ModelInfoCard
import ru.tanexc.hemoanalysis.settings.ui.components.NoModelCard
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsScreen(
    paddingValues: PaddingValues,
    component: SettingsComponent
) {
    val state by component.state.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.padding(
            top = 0.dp,
            bottom = paddingValues.calculateBottomPadding()
        ).fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(Res.string.settings)) }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .hideKeyboardOnTap(focusManager)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Spacer(Modifier.size(8.dp))
            when (val modelInfo = state.currentModelInfo) {
                null -> NoModelCard()
                else -> ModelInfoCard(
                    version = modelInfo.version,
                    size = modelInfo.size,
                    createdAtTime = modelInfo.createdAt,
                    lastUpdateTime = modelInfo.lastUpdateTime
                )
            }

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.apiKey,
                onValueChange = { component.onEvent(SettingsUiEvent.UpdateApiKey(it)) },
                label = {
                    Text(
                        text = stringResource(Res.string.api_key_label),
                        fontWeight = FontWeight.Bold
                    )
                },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    errorContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(36.dp),
                trailingIcon = {
                    if (state.apiKey.isNotEmpty()) {
                        IconButton(
                            onClick = { component.onEvent(SettingsUiEvent.UpdateApiKey("")) }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.cancel),
                                contentDescription = null
                            )
                        }
                    }
                },
                isError = state.apiKey.isEmpty()
            )

            when (val loadingState = state.loadingState) {
                is LoadingState.Success -> ModelInfoButton(
                    status = loadingState.status,
                    isEnabled = true,
                    onClick = {
                        when (loadingState.status) {
                            ModelStatus.UpToDate -> component.onEvent(SettingsUiEvent.UpdateModelInfo)
                            else -> component.onEvent(SettingsUiEvent.LoadLatestModel)
                        }
                    }
                )

                is LoadingState.Error -> ErrorCard(onClick = {
                    when (loadingState.status) {
                        ModelStatus.UpToDate -> component.onEvent(SettingsUiEvent.UpdateModelInfo)
                        else -> component.onEvent(SettingsUiEvent.LoadLatestModel)
                    }
                })

                is LoadingState.Loading -> LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                is LoadingState.ApiKeyNotSet,
                is LoadingState.Idle -> Unit
            }
        }
    }
}

private fun Modifier.hideKeyboardOnTap(focusManager: FocusManager): Modifier =
    pointerInput(focusManager) {
        detectTapGestures {
            focusManager.clearFocus()
        }
    }
