plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.maven.publish)
}

kotlin {
    android {
        namespace = "io.github.mudrichenkoevgeny.kmp.core.security"
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
            implementation(libs.kotlinx.serialization.json)
            api(libs.kotlinx.coroutines.core)

            // Compose
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.runtime)
            implementation(libs.compose.resources)
            implementation(libs.compose.ui.tooling.preview)
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
            implementation(libs.shared.foundation.testing)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }

        getByName("androidHostTest").dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }

        wasmJsTest.dependencies {
            implementation(kotlin("test"))
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

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.mudrichenkoevgeny.kmp.core.security"
}
