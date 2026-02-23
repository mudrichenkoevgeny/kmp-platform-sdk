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
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.maven.publish)
}

kotlin {
    jvmToolchain(libs.versions.jvmToolchain.get().toInt())

    androidTarget()

//    androidLibrary {
//        namespace = "io.github.mudrichenkoevgeny.kmp.core.security"
//        compileSdk = libs.versions.android.compileSdk.get().toInt()
//        minSdk = libs.versions.android.minSdk.get().toInt()
//    }

    // TODO: [IOS] Uncomment when starting iOS development
//    listOf(
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach {
//        it.binaries.framework {
//            baseName = "CoreSecurity"
//            isStatic = true
//        }
//    }

    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            // Project Modules
            implementation(project(":core:common"))

            // Shared Foundation
            implementation(libs.shared.foundation.core.common)
            implementation(libs.shared.foundation.core.security)

            // Kotlin
            api(libs.kotlinx.coroutines.core)

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
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)

            // Infrastructure
            implementation(libs.decompose)

            // Logging
            implementation(libs.kermit)

            // Testing
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            // Storage
            implementation(libs.androidx.datastorePreferences)

            // Android
            implementation(libs.androidx.activityCompose)
            implementation(libs.compose.ui.tooling)
            api(libs.crypto.tink.android)
        }

        // TODO: [IOS] Uncomment when starting iOS development
//        iosMain.dependencies {
//            // Ktor
//            implementation(libs.ktor.client.darwin)
//
//            // Storage
//            implementation(libs.androidx.datastorePreferences)
//        }
    }
}

android {
    namespace = "io.github.mudrichenkoevgeny.kmp.core.security"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.mudrichenkoevgeny.kmp.core.security"
}