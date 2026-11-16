val repoRootDir: File = rootDir.parentFile.parentFile

plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
    alias(libs.plugins.shared.foundation.kotlin)
    alias(libs.plugins.shared.foundation.quality)
    alias(libs.plugins.shared.foundation.publish)
}

gradlePlugin {
    plugins {
        create("kmpAgentSync") {
            id = "io.github.mudrichenkoevgeny.kmp-platform-agent-sync"
            implementationClass = "io.github.mudrichenkoevgeny.kmp.tools.gradleplugin.KmpAgentSyncPlugin"
            displayName = "KMP Platform Agent Sync Gradle Plugin"
            description = "Gradle plugin synchronizing KMP platform AI agent rules into consumer projects"
        }
        create("kmpQuality") {
            id = "io.github.mudrichenkoevgeny.kmp-platform-quality"
            implementationClass = "io.github.mudrichenkoevgeny.kmp.tools.gradleplugin.KmpQualityPlugin"
            displayName = "KMP Platform Quality Gradle Plugin"
            description = "Gradle plugin configuring Detekt with KMP rules and Ktlint"
        }
        create("kmpKotlinConvention") {
            id = "io.github.mudrichenkoevgeny.kmp-platform-kotlin"
            implementationClass = "io.github.mudrichenkoevgeny.kmp.tools.gradleplugin.KmpKotlinConventionPlugin"
            displayName = "KMP Platform Kotlin Convention Gradle Plugin"
            description = "Gradle plugin configuring Kotlin options and explicitApi for KMP modules"
        }
        create("kmpPublishConvention") {
            id = "io.github.mudrichenkoevgeny.kmp-platform-publish"
            implementationClass = "io.github.mudrichenkoevgeny.kmp.tools.gradleplugin.KmpPublishConventionPlugin"
            displayName = "KMP Platform Maven Publishing Convention Gradle Plugin"
            description =
                "Gradle plugin configuring Vanniktech Maven Publishing, Dokka, and POM metadata for KMP modules"
        }
    }
}

dependencies {
    compileOnly(libs.agp.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.detekt.gradle.plugin)
    implementation(libs.ktlint.gradle.plugin)
    implementation(libs.maven.publish.gradle.plugin)
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.shared.foundation.gradle.plugin)
}

val generatePluginResources = tasks.register("generatePluginResources") {
    group = "build"
    description = "Generates plugin version and dependency coordinate resource files"

    val currentVersion = project.version.toString()
    val ktlintComposeRulesNotation = libs.ktlint.compose.rules.get().toString()

    inputs.property("version", currentVersion)
    inputs.property("ktlintComposeRulesNotation", ktlintComposeRulesNotation)

    val outputDir = layout.buildDirectory.dir("generated/resources/plugin-meta")
    outputs.dir(outputDir)

    doLast {
        val dir = outputDir.get().asFile
        dir.mkdirs()
        dir.resolve("kmp-platform-sdk-plugin-version.txt").writeText(currentVersion)
        dir.resolve("ktlint-compose-rules-dependency.txt").writeText(ktlintComposeRulesNotation)
    }
}

tasks.named<ProcessResources>("processResources") {
    from(generatePluginResources)
    from(repoRootDir.resolve(".agent/global/kmp")) {
        into("agent-rules/kmp")
    }
}
