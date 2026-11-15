import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.sqlDelight) apply false
    alias(libs.plugins.buildConfig) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.gradle.versions.plugin)
    alias(libs.plugins.binary.compatibility.validator)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kmp.platform.agent.sync)
}

apiValidation {
    validationDisabled = true
    ignoredProjects += listOf(
        "bom",
        "composeApp",
        "androidApp",
        "detekt-rules",
        "testing"
    )
}

allprojects {
    group = rootProject.group
    version = rootProject.version
}

subprojects {
    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            optIn.addAll(
                "kotlin.uuid.ExperimentalUuidApi",
                "kotlinx.coroutines.ExperimentalCoroutinesApi",
            )
        }
    }
}
