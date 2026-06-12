package ru.tanexc.hemoanalysis.tool.analysis.impl.inference


internal expect class InferenceHandler() {
    operator fun invoke(tensorImage: FloatArray): InferenceOutput
}
