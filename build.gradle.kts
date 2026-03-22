import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.multiplatform).apply(false)
    // TODO: [KMP_ANDROID]
    // Trigger: "Full support for Android Kotlin Multiplatform plugin"
    // Action:
    // 1. Remove id("com.android.library")
    // 2. Add alias(libs.plugins.android.kotlin.multiplatform.library).apply(false)
    id("com.android.library") version "9.0.0" apply false
    alias(libs.plugins.kotlinx.serialization).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.compose).apply(false)
    alias(libs.plugins.sqlDelight).apply(false)
    alias(libs.plugins.buildConfig).apply(false)
    alias(libs.plugins.maven.publish).apply(false)
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

subprojects {
    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
            freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
            freeCompilerArgs.add("-opt-in=androidx.compose.ui.test.ExperimentalTestApi")
        }
    }

    afterEvaluate {
        extensions.findByType<KotlinMultiplatformExtension>()?.apply {
            sourceSets.configureEach {
                languageSettings.optIn("kotlin.uuid.ExperimentalUuidApi")
                languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                languageSettings.optIn("androidx.compose.ui.test.ExperimentalTestApi")
            }
        }
    }

    plugins.withId("com.vanniktech.maven.publish") {
        extensions.configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
            val projectPathName = project.path
                .removePrefix(":")
                .replace(":", "-")

            val baseArtifactId = "kmp-platform-sdk"

            val artifactId = if (projectPathName == "SampleComposeApp") {
                baseArtifactId
            } else {
                "$baseArtifactId-$projectPathName"
            }

            coordinates(
                groupId = "io.github.mudrichenkoevgeny",
                artifactId = artifactId,
                version = "0.0.1"
            )

            publishToMavenCentral()
            signAllPublications()

            pom {
                name.set("KMP Platform SDK")
                description.set("Kotlin Multiplatform library")
                url.set("https://github.com/mudrichenkoevgeny/kmp-platform-sdk")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("mudrichenkoevgeny")
                        name.set("Evgeny Mudrichenko")
                        email.set("evgeny.mudrichenko@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/mudrichenkoevgeny/kmp-platform-sdk.git")
                    developerConnection.set("scm:git:ssh://github.com/mudrichenkoevgeny/kmp-platform-sdk.git")
                    url.set("https://github.com/mudrichenkoevgeny/kmp-platform-sdk")
                }
            }
        }
    }
}