package ru.tanexc.hemoanalysis.settings.di

import com.arkivanov.decompose.ComponentContext
import org.koin.dsl.module
import ru.tanexc.hemoanalysis.navigation.Config

val settingsModule = module {
    factory<SettingsComponent> { (context: ComponentContext, onNavigate: (Config) -> Unit, onBack: () -> Unit) ->
        SettingsComponent(context, onNavigate, onBack, get())
    }
}