package io.github.mudrichenkoevgeny.kmp.tools.projectgenerator

import java.io.File

/**
 * Immutable configuration inputs and version properties required to scaffold a new KMP project.
 */
internal data class ProjectGeneratorConfig(
    val projectName: String,
    val projectTitle: String,
    val projectRepoName: String,
    val projectType: String,
    val groupId: String,
    val packageName: String,
    val packageDirPath: String,
    val targetDir: File,
    val linkLocal: Boolean,
    val skipGit: Boolean,
    val sdkRootDir: File,
    val versionMap: Map<String, String>
) {
    fun getVersion(key: String): String =
        versionMap[key] ?: error("Missing required version property: $key")

    companion object {
        private val KNOWN_VERSION_KEYS = listOf(
            "kmpSdkVersion",
            "sharedFoundationVersion",
            "toolchainJvm",
            "androidCompileSdk",
            "androidMinSdk",
            "androidTargetSdk",
            "kotlinVersion",
            "agpVersion",
            "composeVersion",
            "composeMaterial3Version",
            "detektVersion",
            "ktlintVersion",
            "coroutinesVersion",
            "serializationVersion",
            "ktorVersion",
            "decomposeVersion",
            "robolectricVersion",
            "roborazziVersion",
            "kermitVersion",
            "buildConfigVersion",
            "activityComposeVersion",
            "androidxTestCoreVersion",
            "androidxComposeUiVersion"
        )

        fun fromSystemProperties(): ProjectGeneratorConfig {
            val nameProp = System.getProperty("gradle.prop.name")
                ?: System.getProperty("projectName")
                ?: error("Project name is required. Pass -Pname=<name>")
            val name = sanitizeProjectName(nameProp)

            val projectType = (
                System.getProperty("gradle.prop.type")
                    ?: System.getProperty("projectType")
                    ?: "workspace"
                ).lowercase()
            check(projectType in setOf("workspace", "client", "management")) {
                "Invalid project type '$projectType'. Supported types: workspace, client, management."
            }

            val customGroup = System.getProperty("gradle.prop.groupId")
                ?: System.getProperty("groupId")
                ?: ""
            val groupId = if (customGroup.isNotBlank()) customGroup else "com.${sanitizePackageSegment(name)}"

            val basePkg = if (groupId.endsWith(".kmp")) groupId else "$groupId.kmp"
            val packageName = basePkg.replace('-', '_')
            val packageDirPath = packageName.replace('.', '/')

            val rawSdkDir = File(System.getProperty("sdkRootDir") ?: ".")
            val sdkRootDir = if (File(rawSdkDir, "gradlew").exists()) {
                rawSdkDir
            } else if (rawSdkDir.parentFile?.parentFile != null &&
                File(rawSdkDir.parentFile.parentFile, "gradlew").exists()
            ) {
                rawSdkDir.parentFile.parentFile
            } else {
                rawSdkDir
            }

            val customTargetDir = System.getProperty("gradle.prop.targetDir")
                ?: System.getProperty("targetDir")

            val targetDir = if (!customTargetDir.isNullOrBlank()) {
                File(customTargetDir).let { if (it.isAbsolute) it else File(sdkRootDir, customTargetDir) }
            } else {
                File(sdkRootDir.parentFile, "$name-kmp")
            }

            val linkLocal = (System.getProperty("gradle.prop.linkLocal") ?: System.getProperty("linkLocal")).toBoolean()
            val skipGit = (System.getProperty("gradle.prop.skipGit") ?: System.getProperty("skipGit")).toBoolean()

            val title = name.split('-', '_', '.').joinToString("") { part ->
                part.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }

            val versionMap = KNOWN_VERSION_KEYS.associateWith { key ->
                val valProp = System.getProperty(key)
                check(!valProp.isNullOrBlank()) { "System property '$key' must not be blank" }
                valProp
            }

            return ProjectGeneratorConfig(
                projectName = name,
                projectTitle = title,
                projectRepoName = "$name-kmp",
                projectType = projectType,
                groupId = groupId,
                packageName = packageName,
                packageDirPath = packageDirPath,
                targetDir = targetDir,
                linkLocal = linkLocal,
                skipGit = skipGit,
                sdkRootDir = sdkRootDir,
                versionMap = versionMap
            )
        }

        private fun sanitizeProjectName(rawName: String): String = rawName.trim().lowercase().replace(' ', '-')

        private fun sanitizePackageSegment(raw: String): String = raw.lowercase().replace("-", "")
    }
}
