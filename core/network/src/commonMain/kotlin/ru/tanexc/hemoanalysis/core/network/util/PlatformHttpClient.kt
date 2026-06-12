package ru.tanexc.hemoanalysis.core.network.util

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import org.koin.core.scope.Scope

internal expect fun PlatformHttpClient(declaration: HttpClientConfig<*>.() -> Unit): HttpClient