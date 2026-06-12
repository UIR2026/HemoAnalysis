package ru.tanexc.hemoanalysis.core.network.util

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import ru.tanexc.hemoanalysis.core.network.RequestState

internal suspend inline fun <reified T> HttpClient.get(
    url: String,
    block: HttpRequestBuilder.() -> Unit = {}
): RequestState<T> = runCatching {
    get(
        urlString = url,
        block = block
    ).handleResponse<T>()
}.getOrElse { RequestState.Error(message = it.message ?: "Request error: $url") }

internal suspend inline fun <reified T> HttpResponse.handleResponse(): RequestState<T> = when {
    status.isSuccess() -> RequestState.Success(body())
    else -> RequestState.Error(status.description)
}