package ru.tanexc.hemoanalysis.analysis.di

import com.arkivanov.decompose.ComponentContext
import org.koin.dsl.module
import ru.tanexc.hemoanalysis.navigation.Config

val analysisModule = module {
    factory<AnalysisComponent> { (context: ComponentContext, onNavigate: (Config) -> Unit, onBack: () -> Unit) ->
        AnalysisComponent(context, onNavigate, onBack, get(), get())
    }
}