package ru.tanexc.hemoanalysis.di

import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.tanexc.hemoanalysis.repository.AnalysisParamsRepositoryImpl
import ru.tanexc.hemoanalysis.repository.ModelRepositoryImpl
import ru.tanexc.hemoanalysis.core.database.databaseModule
import ru.tanexc.hemoanalysis.core.network.di.networkModule
import ru.tanexc.hemoanalysis.domain.repository.AnalysisParamsRepository
import ru.tanexc.hemoanalysis.domain.repository.ModelRepository
import ru.tanexc.hemoanalysis.domain.usecase.GetLatestModelFileUseCase
import ru.tanexc.hemoanalysis.domain.usecase.GetModelPathUseCase
import ru.tanexc.hemoanalysis.domain.usecase.SaveModelFileUseCase
import ru.tanexc.hemoanalysis.usecase.GetLatestModelFileUseCaseImpl
import ru.tanexc.hemoanalysis.usecase.GetModelPathUseCaseImpl
import ru.tanexc.hemoanalysis.usecase.SaveModelFileUseCaseImpl

val dataModule = module {
    loadKoinModules(listOf(databaseModule, networkModule))
    singleOf(::GetModelPathUseCaseImpl) bind GetModelPathUseCase::class
    singleOf(::SaveModelFileUseCaseImpl) bind SaveModelFileUseCase::class
    singleOf(::GetLatestModelFileUseCaseImpl) bind GetLatestModelFileUseCase::class

    single<AnalysisParamsRepository> {
        AnalysisParamsRepositoryImpl(
            settings = get(named("AnalysisParamsSettings"))
        )
    }

    single<ModelRepository> {
        ModelRepositoryImpl(
            settings = get(named("ModelInfoSettings")),
            api = get(),
            saveModelFileUseCase = get()
        )
    }
}

