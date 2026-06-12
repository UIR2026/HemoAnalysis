plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    android {
        namespace = "ru.tanexc.hemoanalysis"
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
            baseName = "datakit"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.domain)
                api(projects.core.database)
                api(projects.core.network)
                
                // settings store
                implementation(libs.multiplatform.settings)
                implementation(libs.multiplatform.settings.serialization)

                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.coroutines.core)

                // koin
                implementation(libs.koin.core)

                implementation(libs.kotlinx.datetime)
            }
        }

        androidMain {
            dependencies {
                // datastore
                implementation(libs.androidx.datastore)
                implementation(libs.androidx.datastore.preferences)
            }
        }

        iosMain {
            dependencies {
                // datastore
                implementation(libs.androidx.datastore)
                implementation(libs.androidx.datastore.preferences)
            }
        }
    }
}
