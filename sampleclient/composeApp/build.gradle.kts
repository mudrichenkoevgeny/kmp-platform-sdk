plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.buildConfig)
}

kotlin {
    android {
        namespace = "io.github.mudrichenkoevgeny.kmp.sampleclient.app"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withHostTestBuilder {}.configure {
            isIncludeAndroidResources = true
        }
    }

    // TODO: [IOS] Uncomment when starting iOS development
//    listOf(
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach {
//        it.binaries.framework {
//            baseName = "SampleClientComposeApp"
//            isStatic = true
//        }
//    }

    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.DevServer()).apply {
                    port = 8081
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            // Project Modules
            implementation(project(":core:common"))
            implementation(project(":core:settings"))
            implementation(project(":core:security"))
            implementation(project(":feature:user"))
            implementation(project(":feature:clientuser"))

            // Shared Foundation
            implementation(libs.shared.foundation.core.common)
            implementation(libs.shared.foundation.core.settings)
            implementation(libs.shared.foundation.core.security)
            implementation(libs.shared.foundation.feature.user)

            // Kotlin
            implementation(libs.kotlinx.serialization.json)

            // Compose
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.runtime)
            implementation(libs.compose.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.material3)

            // Ktor
            implementation(libs.ktor.client.core)

            // Infrastructure
            implementation(libs.decompose)
            implementation(libs.decompose.compose)

            // Logging
            implementation(libs.kermit)
        }

        wasmJsMain.dependencies {
            // Project Modules
            implementation(project(":core:common"))
        }

        androidMain.dependencies {
            implementation(libs.compose.ui)
            implementation(libs.compose.runtime)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.shared.foundation.testing)
            implementation(libs.shared.foundation.core.security)
            implementation(libs.shared.foundation.feature.user)
            implementation(libs.ktor.client.mock)
            implementation(libs.ktor.client.cio)
        }

        getByName("androidHostTest").dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.androidx.test.core)
            implementation(libs.compose.ui.test)
            implementation(libs.androidx.compose.ui.test.manifest)
            implementation(libs.robolectric)
            implementation(libs.ktor.client.cio)
        }

        wasmJsTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.mudrichenkoevgeny.kmp.sampleclient.app"
}

val env = project.findProperty("app.env")?.toString() ?: "dev"

buildConfig {
    packageName("io.github.mudrichenkoevgeny.kmp.sampleclient")

    useKotlinOutput { internalVisibility = false }

    buildConfigField("APP_VERSION", "\"${project.version}\"")

    when (env) {
        "prod" -> {
            buildConfigField("BASE_URL", "http://192.168.0.134:8080")
            buildConfigField("ENV_NAME", "Production")
            buildConfigField("GOOGLE_WEB_CLIENT_ID", "982478008604-albtrhoegkqkpe2ev7rirg6c8h9hijfl.apps.googleusercontent.com")
        }
        "test" -> {
            buildConfigField("BASE_URL", "http://192.168.0.134:8080")
            buildConfigField("ENV_NAME", "Testing")
            buildConfigField("GOOGLE_WEB_CLIENT_ID", "982478008604-albtrhoegkqkpe2ev7rirg6c8h9hijfl.apps.googleusercontent.com")
        }
        else -> {
            buildConfigField("BASE_URL", "http://192.168.0.134:8080")
            buildConfigField("ENV_NAME", "Development")
            buildConfigField("GOOGLE_WEB_CLIENT_ID", "982478008604-albtrhoegkqkpe2ev7rirg6c8h9hijfl.apps.googleusercontent.com")
        }
    }
}
