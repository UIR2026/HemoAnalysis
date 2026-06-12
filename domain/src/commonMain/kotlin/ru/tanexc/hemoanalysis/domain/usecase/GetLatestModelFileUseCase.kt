package ru.tanexc.hemoanalysis.domain.usecase

interface GetLatestModelFileUseCase {
    operator fun invoke(): ByteArray?
}