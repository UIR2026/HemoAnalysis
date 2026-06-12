package ru.tanexc.hemoanalysis.analysis.util

import kotlin.math.pow
import kotlin.math.round

fun Float.round(n: Int): Float {
    return round(this * 10f.pow(n)) / 10f.pow(n)
}