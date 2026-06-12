package ru.tanexc.hemoanalysis.tool.analysis.impl.preprocess

import ru.tanexc.hemoanalysis.tool.analysis.impl.core.MODEL_INPUT_HEIGHT
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.MODEL_INPUT_WIDTH
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.blue
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.green
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.red
import ru.tanexc.hemoanalysis.tool.analysis.impl.core.rgb
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.ImageBuffer
import ru.tanexc.hemoanalysis.tool.analysis.impl.data.LetterboxMeta
import ru.tanexc.hemoanalysis.tool.analysis.api.domain.parameters.PreprocessVariant
import kotlin.math.min
import kotlin.math.roundToInt

internal class PreprocessHandler {
    operator fun invoke(
        imageBuffer: ImageBuffer,
        preprocessVariant: PreprocessVariant
    ): PreprocessOutput {
        val buffer = when (preprocessVariant) {
            PreprocessVariant.Default -> imageBuffer
            PreprocessVariant.BrightnessContrast -> brightnessContrast(imageBuffer)
        }

        val letterboxed = letterbox(
            image = buffer,
            targetWidth = MODEL_INPUT_WIDTH,
            targetHeight = MODEL_INPUT_HEIGHT
        )

        return letterboxed
    }

    private fun letterbox(
        image: ImageBuffer,
        targetWidth: Int,
        targetHeight: Int,
        fillRgb: Int = GRAY
    ): PreprocessOutput = with(image) {
        val gain = min(
            MODEL_INPUT_WIDTH.toFloat() / width.toFloat(),
            MODEL_INPUT_HEIGHT.toFloat() / height.toFloat()
        )

        val resizedWidth = (width * gain).roundToInt().coerceAtLeast(1)
        val resizedHeight = (height * gain).roundToInt().coerceAtLeast(1)

        val resized = resizeBilinear(
            image = image,
            targetWidth = resizedWidth,
            targetHeight = resizedHeight
        )

        val padX = (targetWidth - resizedWidth) / 2f
        val padY = (targetHeight - resizedHeight) / 2f

        val left = (padX - 0.1f).roundToInt()
        val top = (padY - 0.1f).roundToInt()

        val out = IntArray(targetWidth * targetHeight) { fillRgb }

        for (y in 0 until resizedHeight) {
            for (x in 0 until resizedWidth) {
                val dstX = left + x
                val dstY = top + y

                if (dstX in 0 until targetWidth && dstY in 0 until targetHeight) {
                    out[dstY * targetWidth + dstX] = resized[x, y]
                }
            }
        }

        return PreprocessOutput(
            imageBuffer = ImageBuffer(
                width = targetWidth,
                height = targetHeight,
                pixels = out
            ),
            meta = LetterboxMeta(
                gain = gain,
                padX = padX,
                padY = padY,
                left = left,
                top = top
            )
        )
    }

    private fun resizeBilinear(
        image: ImageBuffer,
        targetWidth: Int,
        targetHeight: Int
    ): ImageBuffer {
        if (image.width == targetWidth && image.height == targetHeight) {
            return image
        }

        val out = IntArray(targetWidth * targetHeight)

        val xRatio = if (targetWidth > 1) {
            (image.width - 1).toFloat() / (targetWidth - 1).toFloat()
        } else {
            0f
        }

        val yRatio = if (targetHeight > 1) {
            (image.height - 1).toFloat() / (targetHeight - 1).toFloat()
        } else {
            0f
        }

        for (y in 0 until targetHeight) {
            val srcY = y * yRatio
            val y0 = srcY.toInt()
            val y1 = min(y0 + 1, image.height - 1)
            val yWeight = srcY - y0

            for (x in 0 until targetWidth) {
                val srcX = x * xRatio
                val x0 = srcX.toInt()
                val x1 = min(x0 + 1, image.width - 1)
                val xWeight = srcX - x0

                val p00 = image[x0, y0]
                val p10 = image[x1, y0]
                val p01 = image[x0, y1]
                val p11 = image[x1, y1]

                val r = bilinear(red(p00), red(p10), red(p01), red(p11), xWeight, yWeight)
                val g = bilinear(green(p00), green(p10), green(p01), green(p11), xWeight, yWeight)
                val b = bilinear(blue(p00), blue(p10), blue(p01), blue(p11), xWeight, yWeight)

                out[y * targetWidth + x] = rgb(r, g, b)
            }
        }

        return ImageBuffer(
            width = targetWidth,
            height = targetHeight,
            pixels = out
        )
    }

    private fun bilinear(
        v00: Int,
        v10: Int,
        v01: Int,
        v11: Int,
        xWeight: Float,
        yWeight: Float
    ): Int {
        val top = v00 * (1f - xWeight) + v10 * xWeight
        val bottom = v01 * (1f - xWeight) + v11 * xWeight
        return (top * (1f - yWeight) + bottom * yWeight)
            .roundToInt()
            .coerceIn(0, 255)
    }

    companion object {
        private val GRAY: Int = rgb(114, 114, 114)
    }
}

