# Development and Publishing

## Build Environments & Configuration

To build the project with a specific environment, pass the `app.env` property in your terminal:

* **Build for Development:**
  ```bash
  ./gradlew :sample:composeApp:assembleDebug -Papp.env=dev
   ```
  
* **Build for Testing:**
  ```bash
  ./gradlew :sample:composeApp:assembleDebug -Papp.env=test
   ```
  
* **Build for Production:**
  ```bash
  ./gradlew :sample:composeApp:assembleDebug -Papp.env=prod
   ```

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

## Artifact Deployment

The project utilizes the `com.vanniktech.maven.publish` plugin for artifact management.

### 1. Remote Staging
Uploads all publications to the remote staging repository (Maven Central) without performing a final release. Use this for manual verification in the repository manager:

```bash
./gradlew publishAllPublicationsToMavenCentralRepository
```

### 2. Full Release Cycle
Performs a complete deployment workflow, including uploading, closing the staging repository, and releasing artifacts to Maven Central:

```bash
./gradlew publishAndReleaseToMavenCentral
```

## Screenshot Testing (Roborazzi)

The project uses Roborazzi for Compose UI screenshot regression testing.

### 1. Recording / Updating Golden Screenshots
When you create new screenshot tests or intentionally update UI designs, record or update the baseline reference images:

```bash
./gradlew recordRoborazziDebug
```
Or for a specific module:
```bash
./gradlew :feature:clientuser:recordRoborazziDebug
```
Baseline reference images are stored in version control under `src/androidUnitTest/snapshots/images/...`.

### 2. Verifying Screenshots
To run screenshot tests in verification mode (comparing current UI against committed baselines and failing on any discrepancy):

```bash
./gradlew verifyRoborazziDebug
```
Or for a specific module:
```bash
./gradlew :feature:clientuser:verifyRoborazziDebug
```
When verification fails, diff images and HTML reports are generated under `build/outputs/roborazzi/` and `build/reports/roborazzi/index.html`.

