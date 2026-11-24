package io.github.mudrichenkoevgeny.kmp.tools.projectgenerator

import kotlin.test.Test
import kotlin.test.assertEquals

class ProjectGeneratorConfigTest {

    @Test
    fun testDefaultGroupIdAndPackageNameComputation() {
        withSystemProperties(
            mapOf(
                "projectName" to "sample",
                "groupId" to ""
            ) + DUMMY_VERSIONS
        ) {
            val config = ProjectGeneratorConfig.fromSystemProperties()

            assertEquals("com.sample", config.groupId)
            assertEquals("com.sample.kmp", config.packageName)
            assertEquals("com/sample/kmp", config.packageDirPath)
            assertEquals("Sample", config.projectTitle)
            assertEquals("sample-kmp", config.projectRepoName)
        }
    }

    @Test
    fun templateReplacer_replacesTokensCorrectly() {
        withSystemProperties(
            mapOf(
                "projectName" to "sample-app",
                "groupId" to "com.example.sampleapp"
            ) + DUMMY_VERSIONS
        ) {
            val config = ProjectGeneratorConfig.fromSystemProperties()

            val rawTemplate = "project={{PROJECT_NAME}}, title={{PROJECT_TITLE}}, pkg={{PACKAGE_NAME}}, kotlin={{KOTLIN_VERSION}}"
            val evaluated = TemplateReplacer.replaceTokens(rawTemplate, config)

            assertEquals("project=sample-app, title=SampleApp, pkg=com.example.sampleapp.kmp, kotlin=1.0.0", evaluated)
        }
    }

    private fun <T> withSystemProperties(
        properties: Map<String, String>,
        block: () -> T
    ): T {
        val originalValues = properties.keys.associateWith { System.getProperty(it) }
        try {
            properties.forEach { (key, value) ->
                System.setProperty(key, value)
            }
            return block()
        } finally {
            originalValues.forEach { (key, originalValue) ->
                if (originalValue == null) {
                    System.clearProperty(key)
                } else {
                    System.setProperty(key, originalValue)
                }
            }
        }
    }

    private companion object {
        private val DUMMY_VERSIONS: Map<String, String> = mapOf(
            "kmpSdkVersion" to "1.0.0",
            "sharedFoundationVersion" to "1.0.0",
            "toolchainJvm" to "21",
            "androidCompileSdk" to "37",
            "androidMinSdk" to "24",
            "androidTargetSdk" to "37",
            "kotlinVersion" to "1.0.0",
            "agpVersion" to "1.0.0",
            "composeVersion" to "1.0.0",
            "composeMaterial3Version" to "1.0.0",
            "detektVersion" to "1.0.0",
            "ktlintVersion" to "1.0.0",
            "coroutinesVersion" to "1.0.0",
            "serializationVersion" to "1.0.0",
            "ktorVersion" to "1.0.0",
            "decomposeVersion" to "1.0.0",
            "robolectricVersion" to "1.0.0",
            "roborazziVersion" to "1.0.0",
            "kermitVersion" to "1.0.0",
            "buildConfigVersion" to "1.0.0",
            "activityComposeVersion" to "1.0.0",
            "androidxTestCoreVersion" to "1.0.0",
            "androidxComposeUiVersion" to "1.0.0"
        )
    }
}
