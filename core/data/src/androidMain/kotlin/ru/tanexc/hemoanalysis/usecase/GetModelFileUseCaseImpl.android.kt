package ru.tanexc.hemoanalysis.usecase

import android.content.Context
import android.util.Log
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.tanexc.hemoanalysis.Constants.INTERNAL_MODELS_DIR
import ru.tanexc.hemoanalysis.domain.repository.ModelRepository
import ru.tanexc.hemoanalysis.domain.usecase.GetLatestModelFileUseCase
import java.io.File

internal actual class GetLatestModelFileUseCaseImpl : GetLatestModelFileUseCase, KoinComponent {
    private val context: Context by inject()
    private val modelRepository: ModelRepository by inject()

    actual override operator fun invoke(): ByteArray? {
        val filename = modelRepository.getCurrentModelInfo()?.filename?: return null

        val modelsDir = File(context.filesDir, INTERNAL_MODELS_DIR)

        if (!modelsDir.exists()) return null

        val modelFile = File(modelsDir, filename)

        if (!modelFile.exists()) return null

        Log.i("GetLatestModelFileUseCase", "get model bytearray ${modelFile.absolutePath}")

        return modelFile.inputStream().buffered().use { inputStream ->
            inputStream.readBytes()
        }
    }
}