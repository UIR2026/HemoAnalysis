package ru.tanexc.hemoanalysis.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import ru.tanexc.hemoanalysis.analysis.di.AnalysisComponent
import ru.tanexc.hemoanalysis.analysis.ui.AnalysisScreen
import ru.tanexc.hemoanalysis.settings.di.SettingsComponent
import ru.tanexc.hemoanalysis.settings.ui.SettingsScreen

sealed interface Child {
    @Composable
    fun Content(paddingValues: PaddingValues)

    data class Analysis(
        val component: AnalysisComponent
    ): Child {
        @Composable
        override fun Content(paddingValues: PaddingValues) = AnalysisScreen(paddingValues, component)
    }

    data class Settings(
        val component: SettingsComponent
    ): Child {
        @Composable
        override fun Content(paddingValues: PaddingValues) = SettingsScreen(paddingValues, component)
    }

    companion object {
        fun create(
            config: Config,
            context: ComponentContext,
            onNavigate: (Config) -> Unit,
            onBack: () -> Unit
        ): Child = when (config) {
            Config.Analysis -> Analysis(component = getComponent(context, onNavigate, onBack))
            Config.Settings -> Settings(component = getComponent(context, onNavigate, onBack))
        }
    }
}