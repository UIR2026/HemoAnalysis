package ru.tanexc.hemoanalysis.usecase

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.dataWithContentsOfFile
import ru.tanexc.hemoanalysis.Constants.INTERNAL_MODELS_DIR
import ru.tanexc.hemoanalysis.domain.repository.ModelRepository
import ru.tanexc.hemoanalysis.domain.usecase.GetLatestModelFileUseCase
import kotlin.getValue

internal actual class GetLatestModelFileUseCaseImpl : GetLatestModelFileUseCase, KoinComponent {
    private val modelRepository: ModelRepository by inject()

    @OptIn(ExperimentalForeignApi::class)
    actual override operator fun invoke(): ByteArray? {
        val filename = modelRepository.getCurrentModelInfo()?.filename?: return null

        val appSupportDir = getApplicationSupportDir()
        val modelFilePath = "$appSupportDir/$INTERNAL_MODELS_DIR/$filename"

        if (!NSFileManager.defaultManager.fileExistsAtPath(modelFilePath)) return null

        val data = NSData.dataWithContentsOfFile(modelFilePath) ?: return null

        return data.bytes?.readBytes(data.length.toInt())
    }

    private fun getApplicationSupportDir(): String {
        val paths = NSSearchPathForDirectoriesInDomains(
            directory = NSApplicationSupportDirectory,
            domainMask = NSUserDomainMask,
            expandTilde = true
        )

        return paths.first() as? String ?: error("Application Support directory not found")
    }
}