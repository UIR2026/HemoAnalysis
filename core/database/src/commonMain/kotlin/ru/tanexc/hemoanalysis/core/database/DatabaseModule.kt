package ru.tanexc.hemoanalysis.core.database

import com.russhwolf.settings.Settings
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

val databaseModule = module {
    single<Settings>(qualifier = StringQualifier("AnalysisParamsSettings")) { createAnalysisParamsSettings() }
    single<Settings>(qualifier = StringQualifier("ModelInfoSettings")) { createModelInfoSettings() }
}