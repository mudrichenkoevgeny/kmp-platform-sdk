# tools/project-generator

Internal Kotlin JVM module and CLI engine for scaffolding clean, minimal, production-ready downstream KMP application repositories (`<name>-kmp`) on disk in a single command.

Triggered via the root Gradle tasks: **`initProject`** or **`scaffoldProject`**.

---

## Generator Architecture & Core Engine

The project skeleton generator is implemented as an internal (non-published) JVM module under `tools/project-generator/`:

- **[ProjectGenerator]**: Main generator engine. Copies binary infrastructure files (`gradlew`, `gradlew.bat`, `gradle/wrapper/*`, `gradle-daemon-jvm.properties`, `.editorconfig`, `.gitignore`, `local.properties`, `.agent/global/`), walks physical resource templates from `src/main/resources/templates/` (`common/`, `client/`, `management/`), sets executable permissions (`chmod +x gradlew`), enforces overwrite protection (`checkTargetDirectory`), and initializes a Git repository.
- **[ProjectGeneratorConfig]**: Parameter resolution data class. Reads system properties passed by Gradle, validates required version inputs (`KNOWN_VERSION_KEYS`), and computes `groupId`, `packageName`, `packageDirPath`, `projectType`, and `targetDir` with safe defaults and package sanitization (`com.<name>.kmp`).
- **[TemplateReplacer]**: String substitution engine. Replaces generic placeholder tokens (`{{PROJECT_NAME}}`, `{{PROJECT_TITLE}}`, `{{PROJECT_REPO_NAME}}`, `{{GROUP_ID}}`, `{{PACKAGE_NAME}}`, `{{PACKAGE_DIR_PATH}}`, `{{KMP_SDK_VERSION}}`, `{{SF_VERSION}}`, `{{TOOLCHAIN_JVM}}`, `{{ANDROID_COMPILE_SDK}}`, `{{ANDROID_MIN_SDK}}`, `{{ANDROID_TARGET_SDK}}`, `{{KOTLIN_VERSION}}`, `{{AGP_VERSION}}`, `{{COMPOSE_VERSION}}`, `{{COMPOSE_MATERIAL3_VERSION}}`, `{{DETEKT_VERSION}}`, `{{KTLINT_VERSION}}`, `{{COROUTINES_VERSION}}`, `{{SERIALIZATION_VERSION}}`, `{{KTOR_VERSION}}`, `{{DECOMPOSE_VERSION}}`, `{{ROBOLECTRIC_VERSION}}`, `{{ROBORAZZI_VERSION}}`, `{{KERMIT_VERSION}}`, `{{BUILD_CONFIG_VERSION}}`, `{{ACTIVITY_COMPOSE_VERSION}}`, `{{ANDROIDX_TEST_CORE_VERSION}}`, `{{ANDROIDX_COMPOSE_UI_VERSION}}`, `{{MAVEN_LOCAL_REPOS}}`, `{{SETTINGS_INCLUDES}}`, `{{PROJECT_OVERVIEW_MODULES}}`) with dynamic values injected directly from `kmp-platform-sdk`'s catalog (`libs.versions.toml`).
- **[ProjectGeneratorVerifier]**: In-process E2E verification engine. Scaffolds a test project in `build/tmp/scaffold-test/scaffold-test-kmp` and executes `check assemble` tasks inside it to ensure zero template rot.

---

## CLI Task Usage & Parameters

Scaffold a new downstream KMP application repository:

```bash
./gradlew initProject -Pname=sample -Ptype=workspace -PlinkLocal=true
```

### Command Parameter Matrix

| Parameter | Default Value | Description |
| :--- | :--- | :--- |
| **`-Pname`** | *(Required)* | Short name of the project (e.g. `sample`, `shop-app`). Hyphens are sanitized for Kotlin package paths. |
| **`-Ptype`** | `workspace` | Target application scaffold mode: `workspace` (both client & management), `client` (client-only), or `management` (management-only). |
| **`-PgroupId` / `-Pgroup`** | `com.<name>` | Maven group ID (e.g. `com.sample`). If it does not end with `.kmp`, `.kmp` is automatically appended to the base package name (`com.sample.kmp`). |
| **`-PtargetDir`** | `../<name>-kmp` | Absolute or relative target path for the scaffolded repository. |
| **`-PlinkLocal`** | `false` | When `true`, configures `mavenLocal()` first in the generated `settings.gradle.kts` for local development. |
| **`-PskipGit`** | `false` | When `true`, skips running `git init` in the created directory. |

---

## Resource Templates (`src/main/resources/templates/`)

All text-based files for scaffolded repositories are stored as static `.template` resource files under `src/main/resources/templates/`:

- **Root Configurations (`common/`):** `build.gradle.kts.template`, `settings.gradle.kts.template`, `gradle.properties.template`, `gradle/libs.versions.toml.template`, `AGENTS.md.template`, `README.md.template`, `.agent/local/project-overview.md.template`, `.github/workflows/ci.yml.template`.
- **Client Application (`client/`):**
  - `client/androidApp/build.gradle.kts.template`, `AndroidManifest.xml.template`, `MainActivity.kt.template`
  - `client/composeApp/build.gradle.kts.template`
  - `client/composeApp/src/commonMain/kotlin/{{PACKAGE_DIR_PATH}}/client/app/di/ClientAppComponent.kt.template`
  - `client/composeApp/src/commonMain/kotlin/{{PACKAGE_DIR_PATH}}/client/app/usecase/SyncDataRunner.kt.template`
  - `client/composeApp/src/commonMain/kotlin/{{PACKAGE_DIR_PATH}}/client/app/ui/root/RootContent.kt.template`
  - `client/composeApp/src/androidHostTest/kotlin/{{PACKAGE_DIR_PATH}}/client/app/architecture/ArchitectureTest.kt.template`
  - `client/composeApp/src/androidHostTest/kotlin/{{PACKAGE_DIR_PATH}}/client/app/ui/root/RootContentTest.kt.template`
- **Management Application (`management/`):**
  - `management/androidApp/build.gradle.kts.template`, `AndroidManifest.xml.template`, `MainActivity.kt.template`
  - `management/composeApp/build.gradle.kts.template`
  - `management/composeApp/src/commonMain/kotlin/{{PACKAGE_DIR_PATH}}/management/app/di/ManagementAppComponent.kt.template`
  - `management/composeApp/src/commonMain/kotlin/{{PACKAGE_DIR_PATH}}/management/app/usecase/SyncManagementDataRunner.kt.template`
  - `management/composeApp/src/commonMain/kotlin/{{PACKAGE_DIR_PATH}}/management/app/ui/root/RootContent.kt.template`
  - `management/composeApp/src/androidHostTest/kotlin/{{PACKAGE_DIR_PATH}}/management/app/architecture/ArchitectureTest.kt.template`
  - `management/composeApp/src/androidHostTest/kotlin/{{PACKAGE_DIR_PATH}}/management/app/ui/root/RootContentTest.kt.template`

---

## E2E Automated Verification

The generator is continuously verified against template rot and version drift:

```bash
./gradlew verifyProjectGenerator
```

This task compiles `kmp-platform-sdk`, publishes artifacts to `mavenLocal()`, scaffolds a test project in `build/tmp/scaffold-test/scaffold-test-kmp`, and verifies that `check assemble` tasks pass cleanly out of the box inside the scaffolded project.

[ProjectGenerator]: src/main/kotlin/io/github/mudrichenkoevgeny/kmp/tools/projectgenerator/ProjectGenerator.kt
[ProjectGeneratorConfig]: src/main/kotlin/io/github/mudrichenkoevgeny/kmp/tools/projectgenerator/ProjectGeneratorConfig.kt
[TemplateReplacer]: src/main/kotlin/io/github/mudrichenkoevgeny/kmp/tools/projectgenerator/TemplateReplacer.kt
[ProjectGeneratorVerifier]: src/main/kotlin/io/github/mudrichenkoevgeny/kmp/tools/projectgenerator/ProjectGeneratorVerifier.kt
