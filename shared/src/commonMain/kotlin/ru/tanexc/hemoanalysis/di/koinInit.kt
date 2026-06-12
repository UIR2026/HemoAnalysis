package ru.tanexc.hemoanalysis.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import ru.tanexc.hemoanalysis.analysis.di.analysisModule
import ru.tanexc.hemoanalysis.settings.di.settingsModule
import ru.tanexc.hemoanalysis.tool.analysis.impl.di.analysisToolModule

fun koinInit(declaration: KoinApplication.() -> Unit = {}) {
    startKoin {
        declaration()
        modules(dataModule, analysisToolModule, analysisModule, settingsModule)
    }
}