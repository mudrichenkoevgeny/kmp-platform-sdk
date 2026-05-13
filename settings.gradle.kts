pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kmp-platform-sdk"

fun registerModules(parentDir: String, modules: List<String>) {
    modules.forEach { name ->
        val gradlePath = ":$parentDir:$name"
        val folderPath = "$parentDir/$name"

        include(gradlePath)
        project(gradlePath).projectDir = file(folderPath)
    }
}

val coreModules = listOf(
    "common",
    "security",
    "settings"
)
registerModules("core", coreModules)

val featureModules = listOf(
    "user",
    "securityapi",
    "settingsapi"
)
registerModules("feature", featureModules)

include(":bom")

include(":sample:composeApp")
include(":sample:androidApp")
