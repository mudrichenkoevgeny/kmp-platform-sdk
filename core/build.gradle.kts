plugins {
    alias(libs.plugins.kmp.platform.quality)
    alias(libs.plugins.kmp.platform.kotlin)
    alias(libs.plugins.kmp.platform.publish)
    alias(libs.plugins.shared.foundation.design.system)
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.roborazzi)
}

kotlin {
    android {
        namespace = "io.github.mudrichenkoevgeny.kmp.core"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        optimization {
            consumerKeepRules.file("consumer-rules.pro")
        }

        androidResources {
            enable = true
        }

        withHostTestBuilder {}.configure {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.shared.foundation.core)
            api(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.runtime)
            implementation(libs.compose.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.material3)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.websockets)
            implementation(libs.decompose)
            implementation(libs.decompose.compose)
            implementation(libs.kermit)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.shared.foundation.testing)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
        }

        getByName("androidHostTest").dependencies {
            implementation(project(":testing"))
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.shared.foundation.core)
            implementation(libs.shared.foundation.testing)
            implementation(libs.compose.ui.test)
            implementation(libs.androidx.compose.ui.test.junit4)
            implementation(libs.androidx.compose.ui.test.manifest)
            implementation(libs.androidx.test.core)
            implementation(libs.robolectric)
            implementation(libs.roborazzi)
            implementation(libs.roborazzi.compose)
            implementation(libs.roborazzi.junit.rule)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.datastorePreferences)
            implementation(libs.androidx.activityCompose)
            implementation(libs.compose.ui.tooling)
            api(libs.crypto.tink.android)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.mudrichenkoevgeny.kmp.core.generated.resources"
}

sharedDesignSystem {
    generateTokens.set(true)
    extractFonts.set(true)
    iconModules.set(listOf("core-common", "core-storage"))
}
