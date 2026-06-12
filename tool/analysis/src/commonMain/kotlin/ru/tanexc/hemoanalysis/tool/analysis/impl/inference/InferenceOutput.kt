package ru.tanexc.hemoanalysis.tool.analysis.impl.inference


internal data class InferenceOutput(
    val channelCount: Int,
    val candidateCount: Int,
    val outputData: Array<Array<FloatArray>>
)