package ru.tanexc.hemoanalysis.usecase

import android.content.Context
import android.util.Log
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.tanexc.hemoanalysis.Constants.INTERNAL_MODELS_DIR
import ru.tanexc.hemoanalysis.domain.model.Size
import ru.tanexc.hemoanalysis.domain.usecase.BytesReader
import ru.tanexc.hemoanalysis.domain.usecase.SaveModelFileUseCase
import java.io.File

internal actual class SaveModelFileUseCaseImpl : SaveModelFileUseCase, KoinComponent {
    val context: Context by inject()

    actual override suspend operator fun invoke(
        filename: String,
        bytesReader: BytesReader
    ): Size {
        val modelsDir = File(context.filesDir, INTERNAL_MODELS_DIR)
        modelsDir.mkdirs()

        Log.i("SaveModelFileUseCase", "saving model into ${modelsDir.absolutePath}")

        val modelFile = File(modelsDir, filename)

        modelFile.outputStream().buffered().use { output ->
            var bytes = bytesReader.readNext(BUFFER_SIZE)

            while (bytes.isNotEmpty()) {
                output.write(bytes)
                bytes = bytesReader.readNext(BUFFER_SIZE)
            }
        }

        Log.i("SaveModelFileUseCase", "model saved into file $modelFile ${modelFile.length()}")

        return Size(modelFile.length().toInt())
    }

    private val BUFFER_SIZE = 1024 * 1024
}
