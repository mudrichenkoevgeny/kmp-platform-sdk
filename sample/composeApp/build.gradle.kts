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
//        namespace = "io.github.mudrichenkoevgeny.kmp.sample.app"
//        compileSdk = libs.versions.android.compileSdk.get().toInt()
//        minSdk = libs.versions.android.minSdk.get().toInt()
//    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "SampleComposeApp"
            isStatic = true
        }
    }

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
            implementation(project(":feature:user"))

            // Shared Foundation
            implementation(libs.shared.foundation.core.common)

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

            // Infrastructure
            implementation(libs.decompose)
            implementation(libs.decompose.compose)
        }
    }
}

android {
    namespace = "io.github.mudrichenkoevgeny.kmp.sample.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.mudrichenkoevgeny.kmp.sample.app"
}

val env = project.findProperty("app.env")?.toString() ?: "dev"

buildConfig {
    packageName("io.github.mudrichenkoevgeny.kmp.sample")

    buildConfigField("APP_VERSION", project.version.toString())

    when (env) {
        "prod" -> {
            buildConfigField("BASE_URL", "https://www.myapp.com")
            buildConfigField("ENV_NAME", "Production")
        }
        "test" -> {
            buildConfigField("BASE_URL", "https://www.test.myapp.com")
            buildConfigField("ENV_NAME", "Testing")
        }
        else -> {
            buildConfigField("BASE_URL", "https://www.dev.myapp.com")
            buildConfigField("ENV_NAME", "Development")
        }
    }
}