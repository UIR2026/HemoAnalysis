package ru.tanexc.hemoanalysis.tool.analysis.api.domain

data class ImageBuffer(
    val pixels: IntArray,
    val width: Int,
    val height: Int
) {
    operator fun get(x: Int, y: Int): Int =
        pixels[y * width + x]
}