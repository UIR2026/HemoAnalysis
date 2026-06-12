plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    kotlin("native.cocoapods")
}

kotlin {
    android {
        namespace = "ru.tanexc.hemoananlysis.tool.analysis"

        compileSdk {
            version = release(36) {
                minorApiLevel = 1
            }
        }
        minSdk = 26
    }

    val xcfName = "inferenceKit"
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = xcfName
            isStatic = true
        }
    }

    cocoapods {
        version = "1.0"
        ios.deploymentTarget = "15.1"

        framework {
            baseName = "inference"
            isStatic = false
        }

        pod("onnxruntime-objc", version = libs.versions.onnxruntime.get())
    }


    sourceSets {
        commonMain {
            dependencies {
                api(projects.domain)

                implementation(libs.kotlin.stdlib)
                implementation(libs.koin.core)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.onnxruntime.android)
                implementation(libs.compose.components.resources)
            }
        }

        iosMain {
            dependencies {
            }
        }
    }

}
