plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.roborazzi)
}

kotlin {
    android {
        namespace = "io.github.mudrichenkoevgeny.kmp.feature.user"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        androidResources {
            enable = true
        }

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
//            baseName = "FeatureUserApp"
//            isStatic = true
//        }
//
//        it.compilations.getByName("main") {
//            val googleSignIn by cinterops.creating {
//                definitionFile.set(project.file("src/nativeInterop/cinterop/GoogleSignIn.def"))
//            }
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
            implementation(project(":core:settings"))
            implementation(project(":core:security"))

            // Shared Foundation
            implementation(libs.shared.foundation.core.common)
            implementation(libs.shared.foundation.core.settings)
            implementation(libs.shared.foundation.core.security)
            implementation(libs.shared.foundation.feature.user)

            // Kotlin
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)

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
            implementation(libs.decompose.compose)

            // QR Code
            implementation(libs.qrose)

            // Logging
            implementation(libs.kermit)

            // Testing
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.shared.foundation.testing)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
            implementation(libs.ktor.client.cio)
        }

        getByName("androidHostTest").dependencies {
            implementation(project(":core:testing"))
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.androidx.compose.ui.test.junit4)
            implementation(libs.androidx.compose.ui.test.manifest)
            implementation(libs.androidx.test.core)
            implementation(libs.robolectric)
            implementation(libs.roborazzi)
            implementation(libs.roborazzi.compose)
            implementation(libs.roborazzi.junit.rule)
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

            // OAuth
            implementation(libs.androidx.credentials)
            implementation(libs.androidx.credentials.play.services)
            implementation(libs.googleid)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.mudrichenkoevgeny.kmp.feature.user"
}
