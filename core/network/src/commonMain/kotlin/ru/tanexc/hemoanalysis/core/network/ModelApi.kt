package ru.tanexc.hemoanalysis.core.network

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.util.logging.KtorSimpleLogger
import io.ktor.utils.io.availableForRead
import io.ktor.utils.io.readBuffer
import kotlinx.io.readByteArray
import ru.tanexc.hemoanalysis.core.network.response.ModelDownloadResponse
import ru.tanexc.hemoanalysis.core.network.response.ModelMetaResponse
import ru.tanexc.hemoanalysis.core.network.util.get
import ru.tanexc.hemoanalysis.domain.usecase.BytesReader
import kotlin.math.log
import io.ktor.client.request.get as ktorGet

class ModelApi(
    private val httpClient: HttpClient,
) {
    suspend fun getLatestModelInfo(apiKey: String): RequestState<ModelMetaResponse> =
        httpClient.get(url = "$DEFAULT_BASE_URL/models/latest") {
            header("X-API-KEY", apiKey)
        }

    suspend fun downloadLatestModel(
        apiKey: String,
        version: String,
        filename: String
    ): RequestState<ModelDownloadResponse> = runCatching {
        val response: HttpResponse =
            httpClient.ktorGet("$DEFAULT_BASE_URL/models/latest/download") {
                header("X-API-KEY", apiKey)
                parameter("version", version)
                parameter("filename", filename)
                contentType(ContentType.Application.OctetStream)
            }
        when {
            response.status.isSuccess() -> {
                val buffer = response.bodyAsChannel()
                val bytesReader = object : BytesReader {
                    override suspend fun readNext(size: Int): ByteArray {
                        return buffer.readBuffer(size).readByteArray()
                    }
                }
                RequestState.Success(
                    ModelDownloadResponse(
                        bytesReader = bytesReader,
                        totalBytes = buffer.availableForRead
                    )
                )
            }

            response.status == HttpStatusCode.NotFound -> RequestState.Error("Model not found")
            else -> RequestState.Error(response.status.description)
        }
    }.getOrElse {
        RequestState.Error(
            message = it.message ?: "Request error: $DEFAULT_BASE_URL/models/latest/download"
        )
    }
}
