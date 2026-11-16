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

        optimization {
            consumerKeepRules.file("consumer-rules.pro")
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            api(libs.konsist)
            api(libs.kotlinx.coroutines.core)
        }

        androidMain.dependencies {
            api(libs.compose.ui.test)
            api(libs.roborazzi)
            api(libs.roborazzi.compose)
            api(libs.roborazzi.junit.rule)
        }

        jvmMain.dependencies {
            api(libs.kotlinx.coroutines.test)
        }

        jvmTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
