plugins {
    alias(libs.plugins.kmp.platform.quality)
    alias(libs.plugins.kmp.platform.kotlin)
    alias(libs.plugins.kotlin.jvm)
    `java-library`
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(kotlin("test"))
}

fun versionOf(name: String): String =
    rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs").findVersion(name).get().requiredVersion

fun JavaExec.configureVersionProperties() {
    systemProperty("sdkRootDir", rootProject.rootDir.absolutePath)
    systemProperty("kmpSdkVersion", rootProject.version.toString())
    systemProperty("sharedFoundationVersion", versionOf("shared-foundation"))
    systemProperty("toolchainJvm", versionOf("jvmToolchain"))
    systemProperty("androidCompileSdk", versionOf("android-compileSdk"))
    systemProperty("androidMinSdk", versionOf("android-minSdk"))
    systemProperty("androidTargetSdk", versionOf("android-targetSdk"))
    systemProperty("kotlinVersion", versionOf("kotlin"))
    systemProperty("agpVersion", versionOf("agp"))
    systemProperty("composeVersion", versionOf("compose"))
    systemProperty("composeMaterial3Version", versionOf("compose-material3"))
    systemProperty("detektVersion", versionOf("detekt"))
    systemProperty("ktlintVersion", versionOf("ktlint"))
    systemProperty("coroutinesVersion", versionOf("kotlinx-coroutines"))
    systemProperty("serializationVersion", versionOf("kotlinx-serialization"))
    systemProperty("ktorVersion", versionOf("ktor"))
    systemProperty("decomposeVersion", versionOf("decompose"))
    systemProperty("robolectricVersion", versionOf("robolectric"))
    systemProperty("roborazziVersion", versionOf("roborazzi"))
    systemProperty("kermitVersion", versionOf("kermit"))
    systemProperty("buildConfigVersion", versionOf("buildConfig"))
    systemProperty("activityComposeVersion", versionOf("androidx-activityCompose"))
    systemProperty("androidxTestCoreVersion", versionOf("androidx-test-core"))
    systemProperty("androidxComposeUiVersion", versionOf("androidx-compose-ui"))

    val customName = project.providers.gradleProperty("name").orNull
    val customGroup = project.providers.gradleProperty("groupId").orNull
        ?: project.providers.gradleProperty("group").orNull?.takeIf { it != rootProject.group.toString() }
        ?: ""
    val customType = project.providers.gradleProperty("type").orNull
    val customTargetDir = project.providers.gradleProperty("targetDir").orNull
    val customLinkLocal = project.providers.gradleProperty("linkLocal").orNull
    val customSkipGit = project.providers.gradleProperty("skipGit").orNull

    customName?.let { systemProperty("gradle.prop.name", it) }
    if (customGroup.isNotEmpty()) systemProperty("gradle.prop.groupId", customGroup)
    customType?.let { systemProperty("gradle.prop.type", it) }
    customTargetDir?.let { systemProperty("gradle.prop.targetDir", it) }
    customLinkLocal?.let { systemProperty("gradle.prop.linkLocal", it) }
    customSkipGit?.let { systemProperty("gradle.prop.skipGit", it) }
}

val initProject = tasks.register("initProject", JavaExec::class) {
    group = "scaffolding"
    description = "Scaffolds a new KMP application project"
    mainClass.set("io.github.mudrichenkoevgeny.kmp.tools.projectgenerator.ProjectGeneratorKt")
    classpath = sourceSets["main"].runtimeClasspath
    configureVersionProperties()
}

tasks.register("scaffoldProject") {
    group = "scaffolding"
    description = "Alias for initProject"
    dependsOn(initProject)
}

tasks.register("verifyProjectGenerator", JavaExec::class) {
    group = "verification"
    description = "Scaffolds a test project and runs ./gradlew check assemble E2E"
    mainClass.set("io.github.mudrichenkoevgeny.kmp.tools.projectgenerator.ProjectGeneratorVerifierKt")
    classpath = sourceSets["main"].runtimeClasspath
    configureVersionProperties()

    dependsOn(rootProject.getTasksByName("publishToMavenLocal", true))

    val pluginBuild = gradle.includedBuilds.find { it.name.endsWith("gradle-plugin") }
    if (pluginBuild != null) {
        dependsOn(pluginBuild.task(":publishToMavenLocal"))
    }
}
