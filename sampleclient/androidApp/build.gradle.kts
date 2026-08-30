plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "io.github.mudrichenkoevgeny.kmp.sampleclient.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "io.github.mudrichenkoevgeny.kmp.sampleclient.app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    jvmToolchain(libs.versions.jvmToolchain.get().toInt())
}

dependencies {
    // Project Modules
    implementation(project(":core:common"))
    implementation(project(":feature:user"))
    implementation(project(":feature:clientuser"))
    implementation(project(":sampleclient:composeApp"))

    // Shared Foundation
    implementation(libs.shared.foundation.core.common)
    implementation(libs.shared.foundation.feature.user)

    // Android
    implementation(libs.androidx.activityCompose)
    implementation(libs.compose.ui.tooling)
}