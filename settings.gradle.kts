pluginManagement {
    includeBuild("tools/gradle-plugin")
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}

rootProject.name = "kmp-platform-sdk"

fun registerModules(
    group: String,
    modules: List<String>
) {
    modules.forEach { name ->
        val gradlePath = ":$group:$name"
        val folderPath = "$group/$name"
        include(gradlePath)
        project(gradlePath).projectDir = file(folderPath)
    }
}

val coreModules = listOf(
    "common",
    "security",
    "settings",
    "storage",
    "testing"
)
registerModules(
    "core",
    coreModules
)

val featureModules = listOf(
    "user",
    "clientuser",
    "managementuser"
)
registerModules(
    "feature",
    featureModules
)

include(":bom")

include(":sampleclient:composeApp")
include(":sampleclient:androidApp")

include(":samplemanagement:composeApp")
include(":samplemanagement:androidApp")

include(":tools:detekt-rules")
project(":tools:detekt-rules").projectDir = file("tools/detekt-rules")
