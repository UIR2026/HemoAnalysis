package ru.tanexc.hemoanalysis.usecase

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import ru.tanexc.hemoanalysis.Constants.INTERNAL_MODELS_DIR
import ru.tanexc.hemoanalysis.domain.repository.ModelRepository
import ru.tanexc.hemoanalysis.domain.usecase.GetModelPathUseCase
import kotlin.getValue

actual class GetModelPathUseCaseImpl actual constructor() : GetModelPathUseCase, KoinComponent {
    private val modelRepository: ModelRepository by inject()

    actual override fun invoke(): String? {
        val filename = modelRepository.getCurrentModelInfo()?.filename ?: return null
        val modelPath = "${getApplicationSupportDir()}/$INTERNAL_MODELS_DIR/$filename"
        return modelPath.takeIf { NSFileManager.defaultManager.fileExistsAtPath(it) }
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
