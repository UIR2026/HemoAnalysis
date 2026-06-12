package ru.tanexc.hemoanalysis.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.tanexc.hemoanalysis.domain.State
import ru.tanexc.hemoanalysis.domain.model.ModelInfo

interface ModelRepository {
    fun getLatestModelInfo(): ModelInfo?

    fun getCurrentModelInfo(): ModelInfo?

    fun updateModelInfo(): Flow<State<ModelInfo?>>

    fun loadLatestModel(): Flow<State<ModelInfo?>>

    fun getKeyApi(): String

    fun updateApiKey(apiKey: String)
}