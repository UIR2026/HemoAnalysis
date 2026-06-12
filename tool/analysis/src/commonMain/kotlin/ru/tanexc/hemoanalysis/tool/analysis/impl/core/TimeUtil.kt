package ru.tanexc.hemoanalysis.tool.analysis.impl.core

import ru.tanexc.hemoanalysis.tool.analysis.api.domain.AnalysisException
import kotlin.time.Duration
import kotlin.time.TimeSource.Monotonic.markNow

@Throws(AnalysisException::class)
inline fun <T> measuresDuration(block: () -> T): Pair<T, Duration> {
    val mark = markNow()
    try {
        val result = block()
        return result to mark.elapsedNow()
    } catch (exception: Exception) {
        val elapsed = mark.elapsedNow()
        throw AnalysisException(
            exception = exception,
            duration = elapsed,
        )
    }
}