package ru.tanexc.hemoanalysis.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelMetaResponse(
    val version: String,
    @SerialName("created_at")
    val createdAt: String,
    val filename: String
)
