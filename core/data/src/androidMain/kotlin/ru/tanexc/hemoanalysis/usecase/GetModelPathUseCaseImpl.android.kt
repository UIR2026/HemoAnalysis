package ru.tanexc.hemoanalysis.usecase

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.tanexc.hemoanalysis.domain.repository.ModelRepository
import ru.tanexc.hemoanalysis.domain.usecase.GetModelPathUseCase
import kotlin.getValue

actual class GetModelPathUseCaseImpl actual constructor() : GetModelPathUseCase, KoinComponent {
    private val modelRepository: ModelRepository by inject()

    actual override fun invoke(): String? {
        return modelRepository.getCurrentModelInfo()?.filename
    }
}
