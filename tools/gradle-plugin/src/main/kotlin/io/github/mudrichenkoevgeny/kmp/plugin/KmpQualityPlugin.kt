package io.github.mudrichenkoevgeny.kmp.plugin

import io.github.mudrichenkoevgeny.shared.foundation.plugin.utils.QualityUtils
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Gradle plugin configuring Ktlint and Detekt with shared base and KMP-specific rule sets.
 */
public class KmpQualityPlugin : Plugin<Project> {
    /**
     * Applies shared and KMP quality configurations to the target Gradle project.
     *
     * @param target The target Gradle project to configure.
     */
    override fun apply(target: Project) {
        target.pluginManager.apply("io.github.mudrichenkoevgeny.shared-foundation-quality")

        QualityUtils.registerDetektConfigExtraction(
            target = target,
            taskName = "extractDetektKmpConfig",
            classLoader = javaClass.classLoader,
            resourcePath = "config/detekt/detekt-kmp.yml",
            outputFileName = "detekt-kmp.yml"
        )

        QualityUtils.registerEditorConfigLayer(
            target = target,
            classLoader = javaClass.classLoader,
            resourcePath = "config/editorconfig/editorconfig-kmp.txt"
        )

        val pluginVersion = QualityUtils.resolvePluginVersion(
            classLoader = javaClass.classLoader,
            versionResourceName = "kmp-platform-sdk-plugin-version.txt",
            manifestPackage = javaClass.`package`
        )

        QualityUtils.applyDetektRulesDependency(
            target = target,
            expectedRootProjectName = "kmp-platform-sdk",
            rulesProjectPath = ":tools:detekt-rules",
            mavenCoordinates = "io.github.mudrichenkoevgeny:kmp-platform-sdk-detekt-rules",
            pluginVersion = pluginVersion
        )
    }
}
