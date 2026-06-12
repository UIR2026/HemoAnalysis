package ru.tanexc.hemoanalysis.usecase

import ru.tanexc.hemoanalysis.domain.usecase.GetModelPathUseCase

expect class GetModelPathUseCaseImpl() : GetModelPathUseCase {
    override fun invoke(): String?
}
