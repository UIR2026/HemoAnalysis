package ru.tanexc.hemoanalysis.core.network.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.tanexc.hemoanalysis.core.network.ModelApi
import ru.tanexc.hemoanalysis.core.network.util.PlatformHttpClient

val networkModule = module {
    single<HttpClient> {
        PlatformHttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }

            install(Logging) {
                level = LogLevel.HEADERS
            }
        }
    }

    singleOf(::ModelApi)
}
