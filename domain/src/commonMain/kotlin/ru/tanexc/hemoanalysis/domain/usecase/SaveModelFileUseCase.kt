package ru.tanexc.hemoanalysis.domain.usecase

import ru.tanexc.hemoanalysis.domain.model.Size

interface SaveModelFileUseCase {
    suspend operator fun invoke(filename: String, bytesReader: BytesReader): Size
}
