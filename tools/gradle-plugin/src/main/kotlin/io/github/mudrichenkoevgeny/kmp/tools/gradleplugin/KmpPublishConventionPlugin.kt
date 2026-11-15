package io.github.mudrichenkoevgeny.kmp.tools.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Gradle plugin configuring Maven publishing conventions for KMP modules.
 */
public class KmpPublishConventionPlugin : Plugin<Project> {
    /**
     * Applies shared Maven publishing conventions to the target Gradle project.
     *
     * @param target The target Gradle project to configure.
     */
    override fun apply(target: Project) {
        target.pluginManager.apply(SHARED_FOUNDATION_PUBLISH_PLUGIN_ID)
    }

    private companion object {
        private const val SHARED_FOUNDATION_PUBLISH_PLUGIN_ID: String =
            "io.github.mudrichenkoevgeny.shared-foundation-publish"
    }
}
