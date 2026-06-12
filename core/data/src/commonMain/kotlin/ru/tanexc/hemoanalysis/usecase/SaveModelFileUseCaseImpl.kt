package ru.tanexc.hemoanalysis.usecase

import ru.tanexc.hemoanalysis.domain.model.Size
import ru.tanexc.hemoanalysis.domain.usecase.BytesReader
import ru.tanexc.hemoanalysis.domain.usecase.SaveModelFileUseCase

internal expect class SaveModelFileUseCaseImpl(): SaveModelFileUseCase {
    override suspend fun invoke(filename: String, bytesReader: BytesReader): Size
}
