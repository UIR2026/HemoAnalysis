import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    android {
        namespace = "ru.tanexc.hemoanalysis.navigation"

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }

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
            baseName = "navigationkit"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.compose.foundation)

                // decompose
                implementation(libs.decompose)
                implementation(libs.decompose.extensions.compose)
                implementation(libs.decompose.extensions.compose.experimental)
                implementation(libs.essenty.lifecycle)

                // koin
                implementation(libs.koin.core)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.activity.ktx)
            }
        }
    }
}
