package ru.tanexc.hemoanalysis.core.network.util

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp

internal actual fun PlatformHttpClient(declaration: HttpClientConfig<*>.() -> Unit): HttpClient = HttpClient(
    engineFactory = OkHttp,
    block = declaration,
)