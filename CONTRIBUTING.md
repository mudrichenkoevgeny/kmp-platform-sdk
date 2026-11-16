# Development and Publishing

## Tooling & Code Generation (`tools/*`)

- **`tools/detekt-rules`:** Custom Detekt static analysis rules enforcing architectural constraints across consumer applications ([module README](tools/detekt-rules/README.md)).
- **`tools/gradle-plugin`:** Gradle Convention Plugins (`kmp-platform-agent-sync`, `kmp-platform-quality`, `kmp-platform-kotlin`, `kmp-platform-publish`) providing build tasks, quality checks, compiler options, and AI agent rule synchronization across consumer applications ([module README](tools/gradle-plugin/README.md)).
- **`tools/project-generator`:** Internal JVM generator triggered via `./gradlew initProject` / `./gradlew scaffoldProject` for scaffolding clean, minimal, production-ready downstream KMP application repositories (`<name>-kmp`) on disk ([module README](tools/project-generator/README.md)).

---

## Build Environments & Configuration

To build the sample applications with a specific environment, pass the `app.env` property in your terminal:

* **Build for Development:**
  ```bash
  ./gradlew :sampleclient:androidApp:assembleDebug -Papp.env=dev
  ./gradlew :samplemanagement:androidApp:assembleDebug -Papp.env=dev
  ```

* **Build for Testing:**
  ```bash
  ./gradlew :sampleclient:androidApp:assembleDebug -Papp.env=test
  ./gradlew :samplemanagement:androidApp:assembleDebug -Papp.env=test
  ```

* **Build for Production:**
  ```bash
  ./gradlew :sampleclient:androidApp:assembleDebug -Papp.env=prod
  ./gradlew :samplemanagement:androidApp:assembleDebug -Papp.env=prod
  ```

---

## Dependency Analysis

The project uses the `com.autonomousapps.dependency-analysis` plugin to maintain a clean classpath and optimize the artifact size.

### 1. Project-Wide Health Check
To perform a comprehensive audit of unused, transitive, and misconfigured dependencies across the entire project, run:

```bash
./gradlew buildHealth
```

### 2. Single Module Audit
To run the audit for a single module only, use the projectHealth task:

```bash
./gradlew :<module-name>:projectHealth
```

Example for core:common:
```bash
./gradlew :core:common:projectHealth
```

### 3. Dependency Insight
If the health check warns that a dependency "should be declared directly", use this command for a specific module to find which top-level library brought it:

```bash
./gradlew :<module-name>:dependencyInsight --dependency <library-name> --configuration compileClasspath
```

To generate a complete, searchable text report of all dependencies in a specific module:

```bash
./gradlew :<module-name>:dependencies --configuration compileClasspath > build/reports/<module-name>-dependencies-report.txt
```

### 4. Checking for Dependency Updates
To audit up-to-date and outdated dependencies across the project using the `com.github.ben-manes.versions` plugin, run:

```bash
./gradlew dependencyUpdates
```

---

## Verification Workflows

### Architecture & Testing Verification (`:testing`)
To run repository-wide Konsist architecture tests, Detekt, and Ktlint for the `:testing` module:

```bash
./gradlew :testing:check
```

### Project Skeleton Generator Verification
When modifying `:tools:project-generator` or resource templates in `tools/project-generator/src/main/resources/templates/`, verify E2E scaffolding by running:

```bash
./gradlew verifyProjectGenerator
```

This task compiles `kmp-platform-sdk`, publishes artifacts to `mavenLocal()`, scaffolds a test project in `build/tmp/scaffold-test/scaffold-test-kmp`, and verifies that `./gradlew check assemble` passes cleanly out of the box inside the scaffolded project.

---

## Screenshot Testing (Roborazzi)

The project uses Roborazzi for Compose UI screenshot regression testing.

### 1. Recording / Updating Golden Screenshots
When you create new screenshot tests or intentionally update UI designs, record or update the baseline reference images:

```bash
./gradlew recordRoborazzi
```
Or for a specific module:
```bash
./gradlew :feature:clientuser:recordRoborazzi
```
Baseline reference images are stored in version control under `src/androidHostTest/snapshots/images/...`.

### 2. Verifying Screenshots
To run screenshot tests in verification mode (comparing current UI against committed baselines and failing on any discrepancy):

```bash
./gradlew verifyRoborazzi
```
Or for a specific module:
```bash
./gradlew :feature:clientuser:verifyRoborazzi
```

---

## Artifact Deployment

The project utilizes the `com.vanniktech.maven.publish` plugin for artifact management.

### 1. Local Maven Deployment (`mavenLocal`)

Because `tools/gradle-plugin` is configured as an included build (`includeBuild`), running a plain `./gradlew publishToMavenLocal` only publishes library artifacts (`kmp-platform-sdk-bom`, `core-*`, `feature-*`, `detekt-rules`). To publish both libraries and Gradle convention plugins to your machine's local Maven cache (`~/.m2/repository/`), specify both task targets:

1. **Publish all libraries and Gradle convention plugins:**
   ```bash
   ./gradlew publishToMavenLocal
   ./gradlew -p tools/gradle-plugin publishToMavenLocal
   ```

2. **Connect in consumer projects:**
   Add `mavenLocal()` to both `pluginManagement` and `dependencyResolutionManagement` in your consumer project's `settings.gradle.kts`:
   ```kotlin
   pluginManagement {
       repositories {
           mavenLocal() // Required for kmp-platform Gradle convention plugins
           gradlePluginPortal()
           google()
           mavenCentral()
       }
   }

   dependencyResolutionManagement {
       repositories {
           mavenLocal() // Required for kmp-platform library artifacts (BOM, core-*, feature-*)
           mavenCentral()
           google()
       }
   }
   ```

### 2. Remote Staging
Uploads all publications to the remote staging repository (Maven Central) without performing a final release. Use this for manual verification in the repository manager:

```bash
./gradlew publishAllPublicationsToMavenCentralRepository
```

### 3. Full Release Cycle
Performs a complete deployment workflow, including uploading, closing the staging repository, and releasing artifacts to Maven Central:

```bash
./gradlew publishAndReleaseToMavenCentral
```

---

## Testing Local Builds (`shared-foundation`)

To test a locally built version of the `shared-foundation` KMP library in this project:

### 1. Publish Shared Library and Plugins to Local Maven Cache
Because `shared-foundation` also uses an included build (`tools/gradle-plugin`) for its Gradle plugins, publish both library artifacts and Gradle convention plugins in the `shared-foundation` project repository:

```bash
./gradlew publishToMavenLocal :shared-foundation-gradle-plugin:publishToMavenLocal
```

### 2. Connect in Consumer Project (`kmp-platform-sdk`)
1. Ensure `mavenLocal()` is declared in both `pluginManagement` and `dependencyResolutionManagement` in `settings.gradle.kts`:
   ```kotlin
   pluginManagement {
       repositories {
           mavenLocal()
           gradlePluginPortal()
           google()
           mavenCentral()
       }
   }

   dependencyResolutionManagement {
       repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
       repositories {
           mavenLocal()
           mavenCentral()
           google()
       }
   }
   ```
2. Rebuild with `--refresh-dependencies` to force Gradle to pick up the locally published artifacts:
   ```bash
   ./gradlew build --refresh-dependencies
   ```
