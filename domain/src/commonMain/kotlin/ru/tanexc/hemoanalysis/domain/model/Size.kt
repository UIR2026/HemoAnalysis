package ru.tanexc.hemoanalysis.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Size(
    val value: Int
) {
    companion object {
        val ZERO get() = Size(0)
    }
}

val Size.Kb: Float get() = value / 1024f
val Size.Mb: Float get() = Kb / 1024f

