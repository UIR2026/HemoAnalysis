package ru.tanexc.hemoanalysis.domain.usecase

interface BytesReader {
    suspend fun readNext(size: Int): ByteArray
}