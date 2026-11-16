package io.github.mudrichenkoevgeny.kmp.tools.projectgenerator

/**
 * Replaces token placeholders (`{{TOKEN}}`) in string templates using a [ProjectGeneratorConfig].
 */
internal object TemplateReplacer {

    fun replaceTokens(
        content: String,
        config: ProjectGeneratorConfig
    ): String {
        val mavenLocalBlock = if (config.linkLocal) "mavenLocal()\n        " else ""
        val settingsIncludes = when (config.projectType) {
            "client" -> "include(\":client:composeApp\")\ninclude(\":client:androidApp\")"
            "management" -> "include(\":management:composeApp\")\ninclude(\":management:androidApp\")"
            else -> "include(\":client:composeApp\")\ninclude(\":client:androidApp\")\n\ninclude(\":management:composeApp\")\ninclude(\":management:androidApp\")"
        }
        val projectOverviewModules = when (config.projectType) {
            "client" -> """
                - **`client/composeApp`**: KMP client UI, Decompose root components, and dependency injection setup.
                - **`client/androidApp`**: Android client runner application (`MainActivity`).
            """.trimIndent()

            "management" -> """
                - **`management/composeApp`**: KMP management UI, Decompose root components, and dependency injection setup.
                - **`management/androidApp`**: Android management runner application (`MainActivity`).
            """.trimIndent()

            else -> """
                - **`client/composeApp`**: KMP client UI, Decompose root components, and dependency injection setup.
                - **`client/androidApp`**: Android client runner application (`MainActivity`).
                - **`management/composeApp`**: KMP management UI, Decompose root components, and dependency injection setup.
                - **`management/androidApp`**: Android management runner application (`MainActivity`).
            """.trimIndent()
        }

        return content
            .replace("{{PROJECT_NAME}}", config.projectName)
            .replace("{{PROJECT_TITLE}}", config.projectTitle)
            .replace("{{PROJECT_REPO_NAME}}", config.projectRepoName)
            .replace("{{GROUP_ID}}", config.groupId)
            .replace("{{PACKAGE_NAME}}", config.packageName)
            .replace("{{PACKAGE_DIR_PATH}}", config.packageDirPath)
            .replace("{{KMP_SDK_VERSION}}", config.getVersion("kmpSdkVersion"))
            .replace("{{SF_VERSION}}", config.getVersion("sharedFoundationVersion"))
            .replace("{{TOOLCHAIN_JVM}}", config.getVersion("toolchainJvm"))
            .replace("{{ANDROID_COMPILE_SDK}}", config.getVersion("androidCompileSdk"))
            .replace("{{ANDROID_MIN_SDK}}", config.getVersion("androidMinSdk"))
            .replace("{{ANDROID_TARGET_SDK}}", config.getVersion("androidTargetSdk"))
            .replace("{{KOTLIN_VERSION}}", config.getVersion("kotlinVersion"))
            .replace("{{AGP_VERSION}}", config.getVersion("agpVersion"))
            .replace("{{COMPOSE_VERSION}}", config.getVersion("composeVersion"))
            .replace("{{COMPOSE_MATERIAL3_VERSION}}", config.getVersion("composeMaterial3Version"))
            .replace("{{DETEKT_VERSION}}", config.getVersion("detektVersion"))
            .replace("{{KTLINT_VERSION}}", config.getVersion("ktlintVersion"))
            .replace("{{COROUTINES_VERSION}}", config.getVersion("coroutinesVersion"))
            .replace("{{SERIALIZATION_VERSION}}", config.getVersion("serializationVersion"))
            .replace("{{KTOR_VERSION}}", config.getVersion("ktorVersion"))
            .replace("{{DECOMPOSE_VERSION}}", config.getVersion("decomposeVersion"))
            .replace("{{ROBOLECTRIC_VERSION}}", config.getVersion("robolectricVersion"))
            .replace("{{ROBORAZZI_VERSION}}", config.getVersion("roborazziVersion"))
            .replace("{{KERMIT_VERSION}}", config.getVersion("kermitVersion"))
            .replace("{{BUILD_CONFIG_VERSION}}", config.getVersion("buildConfigVersion"))
            .replace("{{ACTIVITY_COMPOSE_VERSION}}", config.getVersion("activityComposeVersion"))
            .replace("{{ANDROIDX_TEST_CORE_VERSION}}", config.getVersion("androidxTestCoreVersion"))
            .replace("{{ANDROIDX_COMPOSE_UI_VERSION}}", config.getVersion("androidxComposeUiVersion"))
            .replace("{{MAVEN_LOCAL_REPOS}}", mavenLocalBlock)
            .replace("{{SETTINGS_INCLUDES}}", settingsIncludes)
            .replace("{{PROJECT_OVERVIEW_MODULES}}", projectOverviewModules)
    }
}
