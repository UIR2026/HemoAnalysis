package ru.tanexc.hemoanalysis.core.network.response

import ru.tanexc.hemoanalysis.domain.usecase.BytesReader

data class ModelDownloadResponse(
    val bytesReader: BytesReader,
    val totalBytes: Int
)
