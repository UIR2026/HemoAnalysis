package ru.tanexc.hemoanalysis.analysis.util

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.ImageBuffer

internal actual val ImageBuffer.imageBitmap: ImageBitmap
    get() = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888).asImageBitmap()