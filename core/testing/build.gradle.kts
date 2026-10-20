plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {
    android {
        namespace = "io.github.mudrichenkoevgeny.kmp.core.testing"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    sourceSets {
        androidMain.dependencies {
            api(libs.compose.ui.test)
            api(libs.roborazzi)
            api(libs.roborazzi.compose)
            api(libs.roborazzi.junit.rule)
        }
    }
}
