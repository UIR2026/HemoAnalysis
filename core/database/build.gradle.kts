plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
}

kotlin {
    android {
        namespace = "ru.tanexc.hemoanalysis.core.database"
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
            baseName = "databasekit"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                // settings store
                implementation(libs.multiplatform.settings)
                implementation(libs.multiplatform.settings.serialization)

                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.coroutines.core)

                // koin
                implementation(libs.koin.core)
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
