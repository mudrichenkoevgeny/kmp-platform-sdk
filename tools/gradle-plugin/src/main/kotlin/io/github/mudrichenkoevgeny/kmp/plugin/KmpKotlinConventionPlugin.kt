package io.github.mudrichenkoevgeny.kmp.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

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
        target.pluginManager.apply("io.github.mudrichenkoevgeny.shared-foundation-kotlin")
    }
}
