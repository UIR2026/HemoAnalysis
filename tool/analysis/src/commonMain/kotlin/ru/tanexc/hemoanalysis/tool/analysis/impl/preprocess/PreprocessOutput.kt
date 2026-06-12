package ru.tanexc.hemoanalysis.tool.analysis.impl.preprocess

import ru.tanexc.hemoanalysis.tool.analysis.api.domain.ImageBuffer
import ru.tanexc.hemoanalysis.tool.analysis.impl.data.LetterboxMeta

internal data class PreprocessOutput(
    val imageBuffer: ImageBuffer,
    val meta: LetterboxMeta
)