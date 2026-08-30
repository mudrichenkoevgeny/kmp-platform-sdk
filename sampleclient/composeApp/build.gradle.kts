plugins {
    alias(libs.plugins.multiplatform)
    // TODO: [KMP_ANDROID]
    // Trigger: "Full support for Android Kotlin Multiplatform plugin"
    // Action:
    // 1. Remove id("com.android.library")
    // 2. Add alias(libs.plugins.android.kotlin.multiplatform.library)
    // 3. Uncomment androidLibrary block
    // 4. Remove android block
    id("com.android.library")
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.buildConfig)
}

kotlin {
    jvmToolchain(libs.versions.jvmToolchain.get().toInt())

    androidTarget()

//    androidLibrary {
//        namespace = "io.github.mudrichenkoevgeny.kmp.sampleclient.app"
//        compileSdk = libs.versions.android.compileSdk.get().toInt()
//        minSdk = libs.versions.android.minSdk.get().toInt()
//    }

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
            implementation(project(":feature:settingsapi"))
            implementation(project(":feature:securityapi"))

            // Shared Foundation
            implementation(libs.shared.foundation.core.common)
            implementation(libs.shared.foundation.feature.user)

            // Kotlin
            implementation(libs.kotlinx.serialization.json)

            // Compose
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.runtime)
            implementation(libs.compose.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.icons.core)
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

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.shared.foundation.core.security)
            implementation(libs.shared.foundation.feature.user)
            implementation(libs.ktor.client.mock)
            implementation(libs.ktor.client.cio)
        }

        androidUnitTest.dependencies {
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

android {
    namespace = "io.github.mudrichenkoevgeny.kmp.sampleclient.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
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
            buildConfigField("BASE_URL", "http://localhost:8080")
            buildConfigField("ENV_NAME", "Production")
            buildConfigField("GOOGLE_WEB_CLIENT_ID", "982478008604-albtrhoegkqkpe2ev7rirg6c8h9hijfl.apps.googleusercontent.com")
        }
        "test" -> {
            buildConfigField("BASE_URL", "http://localhost:8080")
            buildConfigField("ENV_NAME", "Testing")
            buildConfigField("GOOGLE_WEB_CLIENT_ID", "982478008604-albtrhoegkqkpe2ev7rirg6c8h9hijfl.apps.googleusercontent.com")
        }
        else -> {
            buildConfigField("BASE_URL", "http://localhost:8080")
            buildConfigField("ENV_NAME", "Development")
            buildConfigField("GOOGLE_WEB_CLIENT_ID", "982478008604-albtrhoegkqkpe2ev7rirg6c8h9hijfl.apps.googleusercontent.com")
        }
    }
}