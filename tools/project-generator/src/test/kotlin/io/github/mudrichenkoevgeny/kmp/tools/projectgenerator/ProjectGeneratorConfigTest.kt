package io.github.mudrichenkoevgeny.kmp.tools.projectgenerator

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class ProjectGeneratorConfigTest {

    @Test
    fun templateReplacer_replacesTokensCorrectly() {
        val config = createTestConfig()

        val rawTemplate = "project={{PROJECT_NAME}}, title={{PROJECT_TITLE}}, pkg={{PACKAGE_NAME}}"
        val evaluated = TemplateReplacer.replaceTokens(rawTemplate, config)

        assertEquals("project=sample-app, title=SampleApp, pkg=com.example.sampleapp.kmp", evaluated)
    }

    private fun createTestConfig(): ProjectGeneratorConfig {
        val versionMap = mapOf(
            "kmpSdkVersion" to "0.1.0",
            "sharedFoundationVersion" to "0.1.18",
            "toolchainJvm" to "25",
            "androidCompileSdk" to "37",
            "androidMinSdk" to "24",
            "androidTargetSdk" to "37",
            "kotlinVersion" to "2.4.21",
            "agpVersion" to "9.5.1",
            "composeVersion" to "1.12.2",
            "composeMaterial3Version" to "1.9.0",
            "detektVersion" to "2.0.0-alpha.6",
            "ktlintVersion" to "14.2.0",
            "coroutinesVersion" to "1.11.0",
            "serializationVersion" to "1.13.0",
            "ktorVersion" to "3.6.1",
            "decomposeVersion" to "3.6.2",
            "robolectricVersion" to "4.18",
            "roborazziVersion" to "1.79.0",
            "kermitVersion" to "2.2.0",
            "buildConfigVersion" to "6.1.2",
            "activityComposeVersion" to "1.13.0",
            "androidxTestCoreVersion" to "1.7.0",
            "androidxComposeUiVersion" to "1.12.1"
        )

        return ProjectGeneratorConfig(
            projectName = "sample-app",
            projectTitle = "SampleApp",
            projectRepoName = "sample-app-kmp",
            projectType = "workspace",
            groupId = "com.example.sampleapp",
            packageName = "com.example.sampleapp.kmp",
            packageDirPath = "com/example/sampleapp/kmp",
            targetDir = File("build/tmp/test"),
            linkLocal = true,
            skipGit = true,
            sdkRootDir = File("."),
            versionMap = versionMap
        )
    }
}
