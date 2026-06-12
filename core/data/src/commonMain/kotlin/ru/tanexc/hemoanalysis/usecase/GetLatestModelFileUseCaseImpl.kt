package ru.tanexc.hemoanalysis.usecase

import ru.tanexc.hemoanalysis.domain.usecase.GetLatestModelFileUseCase

internal expect class GetLatestModelFileUseCaseImpl(): GetLatestModelFileUseCase {
    override operator fun invoke(): ByteArray?
}