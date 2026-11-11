import com.vanniktech.maven.publish.GradlePlugin
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar
import org.gradle.language.jvm.tasks.ProcessResources
import java.io.File
import java.util.Properties

val repoRootDir: File = rootDir.parentFile.parentFile

val rootProps = Properties().apply {
    val rootPropsFile = repoRootDir.resolve("gradle.properties")
    check(rootPropsFile.exists()) {
        "Root gradle.properties not found at: ${rootPropsFile.absolutePath}"
    }
    rootPropsFile.inputStream().use { load(it) }
}

group = checkNotNull(rootProps.getProperty("group")) { "Missing 'group' property in root gradle.properties" }
version = checkNotNull(rootProps.getProperty("version")) { "Missing 'version' property in root gradle.properties" }

plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
    alias(libs.plugins.maven.publish)
}

kotlin {
    explicitApi()
}

gradlePlugin {
    plugins {
        create("kmpAgentSync") {
            id = "io.github.mudrichenkoevgeny.kmp-platform-agent-sync"
            implementationClass = "io.github.mudrichenkoevgeny.kmp.plugin.KmpAgentSyncPlugin"
            displayName = "KMP Platform Agent Sync Gradle Plugin"
            description = "Gradle plugin synchronizing KMP platform AI agent rules into consumer projects"
        }
        create("kmpQuality") {
            id = "io.github.mudrichenkoevgeny.kmp-platform-quality"
            implementationClass = "io.github.mudrichenkoevgeny.kmp.plugin.KmpQualityPlugin"
            displayName = "KMP Platform Quality Gradle Plugin"
            description = "Gradle plugin configuring Detekt with KMP rules and Ktlint"
        }
        create("kmpKotlinConvention") {
            id = "io.github.mudrichenkoevgeny.kmp-platform-kotlin"
            implementationClass = "io.github.mudrichenkoevgeny.kmp.plugin.KmpKotlinConventionPlugin"
            displayName = "KMP Platform Kotlin Convention Gradle Plugin"
            description = "Gradle plugin configuring Kotlin options and explicitApi for KMP modules"
        }
        create("kmpPublishConvention") {
            id = "io.github.mudrichenkoevgeny.kmp-platform-publish"
            implementationClass = "io.github.mudrichenkoevgeny.kmp.plugin.KmpPublishConventionPlugin"
            displayName = "KMP Platform Maven Publishing Convention Gradle Plugin"
            description = "Gradle plugin configuring Vanniktech Maven Publishing, Dokka, and POM metadata for KMP modules"
        }
    }
}

mavenPublishing {
    coordinates(
        group.toString(),
        "kmp-platform-sdk-gradle-plugin",
        version.toString()
    )

    publishToMavenCentral()
    signAllPublications()

    configure(
        GradlePlugin(
            javadocJar = JavadocJar.Javadoc(),
            sourcesJar = SourcesJar.Sources()
        )
    )
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.detekt.gradle.plugin)
    implementation(libs.ktlint.gradle.plugin)
    implementation(libs.maven.publish.gradle.plugin)
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.shared.foundation.gradle.plugin)
}

val generatePluginVersionResource = tasks.register("generatePluginVersionResource") {
    group = "build"
    description = "Generates plugin version resource file"
    val currentVersion = project.version.toString()
    inputs.property("version", currentVersion)
    val versionFile = layout.buildDirectory.file("generated/resources/kmp-platform-sdk-plugin-version.txt")
    outputs.file(versionFile)
    doLast {
        val file = versionFile.get().asFile
        file.parentFile.mkdirs()
        file.writeText(currentVersion)
    }
}

tasks.named<ProcessResources>("processResources") {
    from(generatePluginVersionResource)
    from(repoRootDir.resolve(".agent/global/kmp")) {
        into("agent-rules/kmp")
    }
}
