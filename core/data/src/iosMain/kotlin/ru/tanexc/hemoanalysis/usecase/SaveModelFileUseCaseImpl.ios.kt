package ru.tanexc.hemoanalysis.usecase

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSData
import platform.Foundation.NSFileHandle
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.closeFile
import platform.Foundation.create
import platform.Foundation.fileHandleForWritingAtPath
import platform.Foundation.seekToEndOfFile
import platform.Foundation.writeData
import ru.tanexc.hemoanalysis.Constants.INTERNAL_MODELS_DIR
import ru.tanexc.hemoanalysis.domain.model.Size
import ru.tanexc.hemoanalysis.domain.usecase.BytesReader
import ru.tanexc.hemoanalysis.domain.usecase.SaveModelFileUseCase

internal actual class SaveModelFileUseCaseImpl actual constructor() :
    SaveModelFileUseCase {
    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual override suspend operator fun invoke(
        filename: String,
        bytesReader: BytesReader
    ): Size {
        val appSupportDir = getApplicationSupportDir()
        val modelDir = "$appSupportDir/$INTERNAL_MODELS_DIR"
        NSFileManager.defaultManager.createDirectoryAtPath(
            path = modelDir,
            withIntermediateDirectories = true,
            attributes = null,
            error = null
        )

        val filePath = "$modelDir/$filename"
        if (NSFileManager.defaultManager.fileExistsAtPath(filePath)) {
            NSFileManager.defaultManager.removeItemAtPath(filePath, error = null)
        }
        check(
            NSFileManager.defaultManager.createFileAtPath(
                path = filePath,
                contents = null,
                attributes = null
            )
        ) { "Cannot create file at $filePath" }

        val handle = NSFileHandle.fileHandleForWritingAtPath(filePath)
            ?: error("Cannot open file for writing at $filePath")
        var totalBytes = 0
        try {
            handle.seekToEndOfFile()

            var bytes = bytesReader.readNext(BUFFER_SIZE)
            while (bytes.isNotEmpty()) {
                handle.writeData(bytes.toNSData())
                totalBytes += bytes.size
                bytes = bytesReader.readNext(BUFFER_SIZE)
            }
        } finally {
            handle.closeFile()
        }

        return Size(totalBytes)
    }

    private fun getApplicationSupportDir(): String {
        val paths = NSSearchPathForDirectoriesInDomains(
            directory = NSApplicationSupportDirectory,
            domainMask = NSUserDomainMask,
            expandTilde = true
        )

        return paths.first() as? String ?: error("Application Support directory not found")
    }

    private val BUFFER_SIZE = 1024 * 1024

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun ByteArray.toNSData(): NSData = memScoped {
        NSData.create(
            bytes = allocArrayOf(this@toNSData),
            length = this@toNSData.size.toULong()
        )
    }
}
