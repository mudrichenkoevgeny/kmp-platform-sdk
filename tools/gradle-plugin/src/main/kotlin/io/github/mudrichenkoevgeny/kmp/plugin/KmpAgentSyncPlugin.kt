package io.github.mudrichenkoevgeny.kmp.plugin

import io.github.mudrichenkoevgeny.shared.foundation.plugin.utils.AgentSyncUtils
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Gradle plugin for synchronizing shared and KMP AI agent rules into consumer projects.
 */
public class KmpAgentSyncPlugin : Plugin<Project> {
    /**
     * Applies the shared and KMP agent sync task configurations to the target Gradle project.
     *
     * @param target The target Gradle project to configure.
     */
    override fun apply(target: Project) {
        if (target != target.rootProject) {
            target.logger.warn("KmpAgentSyncPlugin should only be applied to the root project")
            return
        }

        target.pluginManager.apply("io.github.mudrichenkoevgeny.shared-foundation-agent-sync")

        if (target.rootProject.name != "kmp-platform-sdk") {
            AgentSyncUtils.registerAgentRulesLayer(
                target = target,
                classLoader = javaClass.classLoader,
                resourcePath = "agent-rules/kmp",
                targetSubdir = "kmp"
            )
        }
    }
}
