package ru.tanexc.hemoanalysis.tool.analysis.api.domain

import kotlin.time.Duration

class AnalysisException(
    exception: Exception,
    val duration: Duration
): RuntimeException(exception)