package ru.tanexc.hemoanalysis.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ModelInfo(
    val version: String,
    val lastUpdateTime: String,
    val createdAt: String,
    val filename: String,
    val isLatest: Boolean,
    val size: Size
)