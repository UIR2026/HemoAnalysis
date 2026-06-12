package ru.tanexc.hemoanalysis.analysis.util

import androidx.compose.ui.graphics.ImageBitmap
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.ImageBuffer

internal expect val ImageBuffer.imageBitmap: ImageBitmap

internal val ImageBitmap.imageBuffer: ImageBuffer
    get() = ImageBuffer(
        pixels = IntArray(width * height).apply(::readPixels),
        width = width,
        height = height
    )
