plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    android {
        namespace = "ru.tanexc.hemoanalysis.settings"

        minSdk = 26

        compileSdk {
            version = release(36)
        }

        androidResources {
            enable = true
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "settingskit"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.domain)
                api(projects.core.navigation)
                api(projects.tool.analysis)

                // kotlin
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.coroutines.core)

                // compose
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.androidx.graphics.shapes)

                // decompose
                implementation(libs.decompose)
                implementation(libs.decompose.extensions.compose)
                implementation(libs.decompose.extensions.compose.experimental)
                implementation(libs.essenty.lifecycle)

                // koin
                implementation(libs.koin.core)
            }
        }
    }
}
