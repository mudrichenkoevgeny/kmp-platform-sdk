package io.github.mudrichenkoevgeny.kmp.tools.projectgenerator

import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isDirectory
import kotlin.io.path.readText

/**
 * Entry point and orchestrator for generating KMP application project skeletons.
 */
internal object ProjectGenerator {

    private val STATIC_FILES_TO_COPY = listOf(
        "gradlew",
        "gradlew.bat",
        "gradle/wrapper/gradle-wrapper.jar",
        "gradle/wrapper/gradle-wrapper.properties",
        "gradle/gradle-daemon-jvm.properties",
        ".editorconfig",
        ".gitignore",
        "local.properties"
    )

    fun generate(config: ProjectGeneratorConfig) {
        val targetDir = config.targetDir
        println(
            "Scaffolding KMP application project '${config.projectName}'" +
                " (type: ${config.projectType}) in: ${targetDir.absolutePath}"
        )

        checkTargetDirectory(targetDir)
        targetDir.mkdirs()

        copyStaticFiles(config)
        processTemplates(config)
        copyAgentRules(config)

        if (!config.skipGit) {
            initGitRepository(targetDir)
        }

        println("Successfully generated KMP application project at: ${targetDir.absolutePath}")
    }

    private fun checkTargetDirectory(targetDir: File) {
        if (!targetDir.exists()) return
        val conflictingFiles = listOf("settings.gradle.kts", "build.gradle.kts", "gradlew")
        val existingConflicts = conflictingFiles.filter { File(targetDir, it).exists() }
        if (existingConflicts.isNotEmpty()) {
            error(
                "Target directory '${targetDir.absolutePath}' already contains " +
                    "project files: $existingConflicts. Aborting generation."
            )
        }
    }

    private fun copyStaticFiles(config: ProjectGeneratorConfig) {
        for (relPath in STATIC_FILES_TO_COPY) {
            val source = File(config.sdkRootDir, relPath)
            val dest = File(config.targetDir, relPath)
            dest.parentFile.mkdirs()
            if (source.exists()) {
                source.copyTo(dest, overwrite = true)
            }
        }

        val gradlew = File(config.targetDir, "gradlew")
        if (gradlew.exists()) {
            gradlew.setExecutable(true, false)
        }
    }

    private fun processTemplates(config: ProjectGeneratorConfig) {
        walkAndCopyTemplates("common", config.targetDir, config)

        if (config.projectType == "workspace" || config.projectType == "client") {
            val clientTarget = File(config.targetDir, "client")
            clientTarget.mkdirs()
            walkAndCopyTemplates("client", clientTarget, config)
        }

        if (config.projectType == "workspace" || config.projectType == "management") {
            val managementTarget = File(config.targetDir, "management")
            managementTarget.mkdirs()
            walkAndCopyTemplates("management", managementTarget, config)
        }
    }

    private fun walkAndCopyTemplates(
        templatesSubDir: String,
        targetRoot: File,
        config: ProjectGeneratorConfig
    ) {
        val resourcePath = "/templates/$templatesSubDir"
        val resourceUrl = javaClass.getResource(resourcePath)
            ?: error("Templates resource directory not found: $resourcePath")

        val uri = resourceUrl.toURI()
        if (uri.scheme == "jar") {
            val fs = try {
                FileSystems.getFileSystem(uri)
            } catch (_: Exception) {
                FileSystems.newFileSystem(uri, emptyMap<String, Any>())
            }
            val rootPath = fs.getPath(resourcePath)
            walkPathTree(rootPath, targetRoot, config)
        } else {
            val rootPath = Paths.get(uri)
            walkPathTree(rootPath, targetRoot, config)
        }
    }

    private fun walkPathTree(
        rootPath: Path,
        targetRoot: File,
        config: ProjectGeneratorConfig
    ) {
        Files.walk(rootPath).use { stream ->
            stream.filter { !it.isDirectory() }.forEach { path ->
                val relPath = rootPath.relativize(path).toString().replace('\\', '/')
                val rawContent = path.readText()
                val evaluatedContent = TemplateReplacer.replaceTokens(rawContent, config)

                val evaluatedRelPath = relPath
                    .replace("{{PACKAGE_DIR_PATH}}", config.packageDirPath)
                    .removeSuffix(".template")

                val destFile = File(targetRoot, evaluatedRelPath)
                destFile.parentFile.mkdirs()
                destFile.writeText(evaluatedContent)
            }
        }
    }

    private fun copyAgentRules(config: ProjectGeneratorConfig) {
        val sourceAgentGlobal = File(config.sdkRootDir, ".agent/global")
        val destAgentGlobal = File(config.targetDir, ".agent/global")
        if (sourceAgentGlobal.exists()) {
            sourceAgentGlobal.copyRecursively(destAgentGlobal, overwrite = true)
        }
    }

    private fun initGitRepository(targetDir: File) {
        try {
            val process = ProcessBuilder("git", "init")
                .directory(targetDir)
                .redirectErrorStream(true)
                .start()
            process.waitFor()
        } catch (_: Exception) {
            println("Warning: 'git init' execution failed. Continuing scaffolding.")
        }
    }
}

internal fun main() {
    val config = ProjectGeneratorConfig.fromSystemProperties()
    ProjectGenerator.generate(config)
}
