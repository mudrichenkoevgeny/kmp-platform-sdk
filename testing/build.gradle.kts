plugins {
    alias(libs.plugins.kmp.platform.quality)
    alias(libs.plugins.kmp.platform.kotlin)
    alias(libs.plugins.kmp.platform.publish)
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {
    android {
        namespace = "io.github.mudrichenkoevgeny.kmp.testing"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            api(libs.compose.ui.test)
            api(libs.roborazzi)
            api(libs.roborazzi.compose)
            api(libs.roborazzi.junit.rule)
        }

        jvmTest.dependencies {
            implementation(libs.konsist)
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
