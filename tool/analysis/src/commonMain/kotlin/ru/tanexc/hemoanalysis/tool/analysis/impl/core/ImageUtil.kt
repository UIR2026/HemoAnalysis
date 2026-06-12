package ru.tanexc.hemoanalysis.tool.analysis.impl.core

import ru.tanexc.hemoanalysis.tool.analysis.api.domain.ImageBuffer

internal val BACKGROUND_COLOR = rgb(200, 200, 200)

internal fun rgb(
    r: Int,
    g: Int,
    b: Int,
    a: Int = 255
): Int =
    ((a.coerceIn(0, 255) and 0xFF) shl 24) or
            ((r.coerceIn(0, 255) and 0xFF) shl 16) or
            ((g.coerceIn(0, 255) and 0xFF) shl 8) or
            (b.coerceIn(0, 255) and 0xFF)

internal fun red(pixel: Int): Int =
    pixel shr 16 and 0xFF

internal fun green(pixel: Int): Int =
    pixel shr 8 and 0xFF

internal fun blue(pixel: Int): Int =
    pixel and 0xFF


fun gray(pixel: Int): Int {
    val r = pixel ushr 16 and 0xFF
    val g = pixel ushr 8 and 0xFF
    val b = pixel and 0xFF
    return ((r * 299 + g * 587 + b * 114) / 1000).coerceIn(0, 255)
}


internal fun open(mask: BooleanArray, width: Int, height: Int): BooleanArray =
    dilate(erode(mask, width, height), width, height)

internal fun close(mask: BooleanArray, width: Int, height: Int): BooleanArray =
    erode(dilate(mask, width, height), width, height)

internal fun erode(mask: BooleanArray, width: Int, height: Int): BooleanArray {
    val result = BooleanArray(mask.size)
    for (y in 0 until height) {
        for (x in 0 until width) {
            var keep = true
            for (dy in -2..2) {
                for (dx in -2..2) {
                    val nx = x + dx
                    val ny = y + dy
                    if (nx !in 0 until width || ny !in 0 until height || !mask[ny * width + nx]) {
                        keep = false
                        break
                    }
                }
                if (!keep) break
            }
            result[y * width + x] = keep
        }
    }
    return result
}

internal fun dilate(mask: BooleanArray, width: Int, height: Int): BooleanArray {
    val result = BooleanArray(mask.size)
    for (y in 0 until height) {
        for (x in 0 until width) {
            var keep = false
            for (dy in -2..2) {
                for (dx in -2..2) {
                    val nx = x + dx
                    val ny = y + dy
                    if (nx in 0 until width && ny in 0 until height && mask[ny * width + nx]) {
                        keep = true
                        break
                    }
                }
                if (keep) break
            }
            result[y * width + x] = keep
        }
    }
    return result
}

internal fun otsuThreshold(imageBuffer: ImageBuffer): Int {
    val histogram = IntArray(256)
    imageBuffer.pixels.forEach { histogram[gray(it)]++ }
    val total = imageBuffer.pixels.size
    var sum = 0L
    for (i in histogram.indices) sum += i.toLong() * histogram[i]

    var backgroundWeight = 0
    var backgroundSum = 0L
    var bestThreshold = 0
    var bestVariance = -1.0

    for (threshold in histogram.indices) {
        backgroundWeight += histogram[threshold]
        if (backgroundWeight == 0) continue

        val foregroundWeight = total - backgroundWeight
        if (foregroundWeight == 0) break

        backgroundSum += threshold.toLong() * histogram[threshold]
        val backgroundMean = backgroundSum.toDouble() / backgroundWeight
        val foregroundMean = (sum - backgroundSum).toDouble() / foregroundWeight
        val variance = backgroundWeight.toDouble() *
                foregroundWeight *
                (backgroundMean - foregroundMean) *
                (backgroundMean - foregroundMean)

        if (variance > bestVariance) {
            bestVariance = variance
            bestThreshold = threshold
        }
    }

    return bestThreshold
}