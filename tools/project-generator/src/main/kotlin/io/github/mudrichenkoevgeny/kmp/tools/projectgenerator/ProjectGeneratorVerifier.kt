package io.github.mudrichenkoevgeny.kmp.tools.projectgenerator

import java.io.File

/**
 * End-to-end verification harness for the Project Skeleton Generator.
 *
 * Scaffolds a test project, runs `./gradlew check assemble`, and verifies exit code 0 and artifact outputs.
 */
internal object ProjectGeneratorVerifier {

    fun verify() {
        System.setProperty("gradle.prop.name", "scaffold-test")
        System.setProperty("gradle.prop.groupId", "com.example.scaffold")
        System.setProperty("gradle.prop.type", "workspace")
        System.setProperty("gradle.prop.linkLocal", "true")
        System.setProperty("gradle.prop.skipGit", "true")

        val rawSdkDir = File(System.getProperty("sdkRootDir") ?: ".")
        val sdkRootDir = if (File(rawSdkDir, "gradlew").exists()) {
            rawSdkDir
        } else if (rawSdkDir.parentFile?.parentFile != null &&
            File(
                rawSdkDir.parentFile.parentFile,
                "gradlew"
            ).exists()
        ) {
            rawSdkDir.parentFile.parentFile
        } else {
            rawSdkDir
        }

        val scaffoldBaseDir = File(sdkRootDir, "build/tmp/scaffold-test")
        val targetDir = File(scaffoldBaseDir, "scaffold-test-kmp")
        if (targetDir.exists()) {
            targetDir.deleteRecursively()
        }
        scaffoldBaseDir.mkdirs()

        System.setProperty("gradle.prop.targetDir", targetDir.absolutePath)

        val config = ProjectGeneratorConfig.fromSystemProperties()
        ProjectGenerator.generate(config)

        val isWindows = System.getProperty("os.name").lowercase().contains("win")
        val command = if (isWindows) {
            listOf("cmd.exe", "/c", ".\\gradlew.bat", "check", "assemble", "--no-daemon", "--stacktrace")
        } else {
            listOf("./gradlew", "check", "assemble", "--no-daemon", "--stacktrace")
        }

        println("Running E2E build check inside generated project: ${targetDir.absolutePath}")

        val pb = ProcessBuilder(command)
            .directory(targetDir)
            .redirectErrorStream(true)

        val javaHome = System.getProperty("java.home")
        if (!javaHome.isNullOrBlank()) {
            pb.environment()["JAVA_HOME"] = javaHome
        }

        val process = pb.start()

        process.inputStream.bufferedReader().use { reader ->
            var line: String? = reader.readLine()
            while (line != null) {
                println("[ScaffoldE2E] $line")
                line = reader.readLine()
            }
        }

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            error("ProjectGenerator E2E verification failed with exit code $exitCode")
        }

        val clientApkDir = File(targetDir, "client/androidApp/build/outputs/apk/debug")
        if (!clientApkDir.exists() || clientApkDir.listFiles().isNullOrEmpty()) {
            error("Expected Client Android debug APK output does not exist: ${clientApkDir.absolutePath}")
        }

        val managementApkDir = File(targetDir, "management/androidApp/build/outputs/apk/debug")
        if (!managementApkDir.exists() || managementApkDir.listFiles().isNullOrEmpty()) {
            error("Expected Management Android debug APK output does not exist: ${managementApkDir.absolutePath}")
        }

        println("ProjectGenerator E2E verification completed successfully!")
    }
}

internal fun main() {
    ProjectGeneratorVerifier.verify()
}
