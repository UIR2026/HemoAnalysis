package ru.tanexc.hemoanalysis.repository

import com.russhwolf.settings.Settings
import com.russhwolf.settings.string
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import org.koin.core.logger.Logger
import ru.tanexc.hemoanalysis.core.network.ModelApi
import ru.tanexc.hemoanalysis.core.network.RequestState
import ru.tanexc.hemoanalysis.domain.State
import ru.tanexc.hemoanalysis.domain.model.ModelInfo
import ru.tanexc.hemoanalysis.domain.model.Size
import ru.tanexc.hemoanalysis.domain.repository.ModelRepository
import ru.tanexc.hemoanalysis.domain.usecase.SaveModelFileUseCase
import kotlin.time.Clock
import kotlin.time.Instant

internal class ModelRepositoryImpl(
    settings: Settings,
    private val api: ModelApi,
    private val saveModelFileUseCase: SaveModelFileUseCase,
) : ModelRepository {
    private var currentModelInfo: String by settings.string("current_model_info", "")
    private var latestModelInfo: String by settings.string("latest_model_info", "")
    private var apiKey: String by settings.string("api_key", "")

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun getLatestModelInfo(): ModelInfo? = runCatching {
        json.decodeFromString<ModelInfo>(latestModelInfo)
    }.getOrNull()

    override fun getCurrentModelInfo(): ModelInfo? = runCatching {
        json.decodeFromString<ModelInfo>(currentModelInfo)
    }.getOrNull()

    override fun updateModelInfo(): Flow<State<ModelInfo?>> = flow {
        emit(State.Loading)
        when (val state = api.getLatestModelInfo(apiKey)) {
            is RequestState.Error -> emit(State.Error)
            is RequestState.Success -> {
                val timeNow = nowFormatted()
                val meta = state.data
                val currentInfo = getCurrentModelInfo()?.copy(
                    lastUpdateTime = timeNow
                )
                currentModelInfo = currentInfo.asString()

                if (meta.version.isBiggerVersion(currentInfo?.version ?: "")) {
                    val modelInfo = ModelInfo(
                        version = meta.version,
                        lastUpdateTime = timeNow,
                        createdAt = meta.createdAt.toDisplayDateTime(),
                        filename = meta.filename,
                        isLatest = true,
                        size = Size.ZERO
                    )

                    currentModelInfo = currentInfo?.copy(
                        isLatest = false
                    ).asString()

                    latestModelInfo = modelInfo.asString()
                    emit(State.Success(modelInfo))
                } else {
                    emit(State.Success(currentInfo))
                }
            }
        }
    }

    override fun loadLatestModel(): Flow<State<ModelInfo?>> = flow {
        emit(State.Loading)
        val latestInfo = getLatestModelInfo()
        if (latestInfo == null) {
            emit(State.Error)
            return@flow
        }

        val state = api.downloadLatestModel(
            version = latestInfo.version,
            filename = latestInfo.filename,
            apiKey = apiKey
        )

        when (state) {
            is RequestState.Error -> emit(State.Error)
            is RequestState.Success -> {
                val savedSize = saveModelFileUseCase(latestInfo.filename, state.data.bytesReader)

                val currentInfo = latestInfo.copy(
                    lastUpdateTime = nowFormatted(),
                    isLatest = true,
                    size = savedSize
                )

                currentModelInfo = currentInfo.asString()
                emit(State.Success(currentInfo))
            }
        }
    }

    override fun getKeyApi(): String = apiKey

    override fun updateApiKey(apiKey: String) {
        this.apiKey = apiKey
    }

    private fun String.toVersionParts(): List<Int> {
        return split(".").map { part ->
            part.toIntOrNull() ?: 0
        }
    }

    private fun String.isBiggerVersion(
        second: String
    ): Boolean {
        val firstParts = this.toVersionParts()
        val secondParts = second.toVersionParts()

        val maxSize = maxOf(firstParts.size, secondParts.size)

        for (index in 0 until maxSize) {
            val firstValue = firstParts.getOrElse(index) { 0 }
            val secondValue = secondParts.getOrElse(index) { 0 }

            if (firstValue > secondValue) {
                return true
            }
        }

        return false
    }

    private fun ModelInfo?.asString() = this?.let(json::encodeToString) ?: ""

    private fun nowFormatted(): String {
        return Clock.System.now().toDisplayDateTime()
    }

    private fun String.toDisplayDateTime(): String {
        if (runCatching { displayDateTimeFormat.parse(this) }.isSuccess) return this

        return runCatching {
            Instant.parse(this).toDisplayDateTime()
        }.getOrElse {
            runCatching {
                LocalDateTime.parse(this).toDisplayDateTime()
            }.getOrDefault(this)
        }
    }

    private fun Instant.toDisplayDateTime(): String {
        return toLocalDateTime(TimeZone.currentSystemDefault()).toDisplayDateTime()
    }

    private fun LocalDateTime.toDisplayDateTime(): String {
        return format(displayDateTimeFormat)
    }

    companion object {
        private val displayDateTimeFormat: DateTimeFormat<LocalDateTime> = LocalDateTime.Format {
            day()
            char('.')
            monthNumber()
            char('.')
            year()
            char(' ')
            hour()
            char(':')
            minute()
        }
    }
}
