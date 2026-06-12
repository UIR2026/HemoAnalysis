plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    android {
        namespace = "ru.tanexc.hemoanalysis.core.network"
        compileSdk {
            version = release(36)
        }
        minSdk = 26
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "nwtworkKit"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.domain)
                implementation(libs.kotlinx.coroutines.core)

                // koin
                implementation(libs.koin.core)

                // ktor
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.serialization.kotlinx.json)

            }
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}
