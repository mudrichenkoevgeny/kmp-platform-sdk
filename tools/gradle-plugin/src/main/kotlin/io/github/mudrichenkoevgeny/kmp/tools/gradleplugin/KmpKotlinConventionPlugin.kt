package io.github.mudrichenkoevgeny.kmp.tools.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Gradle plugin configuring Kotlin compiler options and opt-ins for KMP modules.
 */
public class KmpKotlinConventionPlugin : Plugin<Project> {
    /**
     * Applies shared Kotlin conventions to the target project.
     *
     * @param target The target Gradle project to configure.
     */
    override fun apply(target: Project) {
        target.pluginManager.apply(SHARED_FOUNDATION_KOTLIN_PLUGIN_ID)

        if (!isKmpPlatformSdk(target)) {
            target.plugins.withId(PLUGIN_KOTLIN_MULTIPLATFORM) {
                target.extensions.configure(KotlinMultiplatformExtension::class.java) { kotlin ->
                    kotlin.explicitApi = ExplicitApiMode.Disabled
                }
            }
        }
    }

    private fun isKmpPlatformSdk(target: Project): Boolean =
        target.rootProject.name == KMP_SDK_ROOT_PROJECT_NAME ||
            target.group.toString().startsWith(KMP_SDK_GROUP_PREFIX)

    private companion object {
        private const val SHARED_FOUNDATION_KOTLIN_PLUGIN_ID: String =
            "io.github.mudrichenkoevgeny.shared-foundation-kotlin"
        private const val PLUGIN_KOTLIN_MULTIPLATFORM: String =
            "org.jetbrains.kotlin.multiplatform"
        private const val KMP_SDK_ROOT_PROJECT_NAME: String =
            "kmp-platform-sdk"
        private const val KMP_SDK_GROUP_PREFIX: String =
            "io.github.mudrichenkoevgeny.kmp"
    }
}
