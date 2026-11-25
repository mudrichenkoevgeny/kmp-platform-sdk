# Contributing to kmp-platform-sdk

## Branching & Pull Requests

- **`main`**: Stable production branch.
- **`develop`**: Active development branch.
- All pull requests target `main` or `develop` and must pass CI (`ci.yml`): Gitleaks secret scanning, `check`, `apiCheck`, `verifyRoborazzi`, and sample Android app builds (`:sampleclient:androidApp:assembleDebug`, `:samplemanagement:androidApp:assembleDebug`).
- All public API declarations require complete English KDoc. Inline implementation comments (`//`, `/* ... */`) and fully qualified names in executable code are prohibited.

---

## Verification & Quality Checks

Run relevant checks before committing:

| Task | Command |
|---|---|
| Format check | `./gradlew ktlintCheck` |
| Format auto-fix | `./gradlew ktlintFormat` |
| Static analysis & custom Detekt rules | `./gradlew detekt` |
| Binary compatibility check | `./gradlew apiCheck` |
| Binary compatibility dump update | `./gradlew apiDump` |
| Screenshot verification (Roborazzi) | `./gradlew verifyRoborazzi` |
| Screenshot baseline update (Roborazzi) | `./gradlew recordRoborazzi` |
| Full verification suite | `./gradlew check` |
| Architecture & testing verification (`:testing`) | `./gradlew :testing:check` |
| Project scaffolding E2E check | `./gradlew verifyProjectGenerator` |
| Dependency health audit (project-wide) | `./gradlew buildHealth` |
| Dependency health audit (single module) | `./gradlew :<module>:projectHealth` |
| Dependency updates check | `./gradlew dependencyUpdates` |
| Dokka HTML documentation generation | `./gradlew dokkaGenerate` |

### Screenshot Testing (Roborazzi)

Golden reference images are committed under `src/androidHostTest/snapshots/images/...`. Run screenshot tasks for a single module when iterating on UI changes:

```bash
./gradlew :feature:clientuser:recordRoborazzi
./gradlew :feature:clientuser:verifyRoborazzi
```

### Dependency Insight

To inspect how a transitive dependency reaches a module:

```bash
./gradlew :<module>:dependencyInsight --dependency <coordinates> --configuration compileClasspath
```

---

## Sample Applications & Tooling

### Sample App Build Environments

Pass `-Papp.env=<dev|test|prod>` when assembling sample applications:

```bash
./gradlew :sampleclient:androidApp:assembleDebug -Papp.env=dev
./gradlew :samplemanagement:androidApp:assembleDebug -Papp.env=dev
```

### Tooling (`tools/*`)

- **`tools/detekt-rules`:** Custom Detekt static analysis rules ([README](tools/detekt-rules/README.md)).
- **`tools/gradle-plugin`:** Gradle convention plugins (`kmp-platform-agent-sync`, `kmp-platform-quality`, `kmp-platform-kotlin`, `kmp-platform-publish`) ([README](tools/gradle-plugin/README.md)).
- **`tools/project-generator`:** Downstream KMP repository scaffolder triggered via `initProject` or `scaffoldProject` tasks ([README](tools/project-generator/README.md)).

---

## Local Testing (`mavenLocal`)

### 1. Publish `kmp-platform-sdk` for Consumer Projects

Because `tools/gradle-plugin` is an included build, publish both library artifacts and Gradle convention plugins to `~/.m2/repository/`:

```bash
./gradlew publishToMavenLocal
./gradlew -p tools/gradle-plugin publishToMavenLocal
```

Add `mavenLocal()` at the top of `pluginManagement` and `dependencyResolutionManagement` in the consumer project's `settings.gradle.kts`.

### 2. Test a Local Build of `shared-foundation`

1. In the `shared-foundation` repository, publish both library artifacts and convention plugins to `mavenLocal`:
   ```bash
   ./gradlew publishToMavenLocal :gradle-plugin:publishToMavenLocal
   ```
2. Ensure `mavenLocal()` is listed in both `pluginManagement` and `dependencyResolutionManagement` in `settings.gradle.kts`, then rebuild with refreshed dependencies:
   ```bash
   ./gradlew build --refresh-dependencies
   ```

---

## Releases

Releases are automated via `.github/workflows/release.yml` on pushing a `v*` tag. The workflow publishes:

- **Maven Central (GPG-signed):** `kmp-platform-sdk-gradle-plugin`, `kmp-platform-sdk-bom`, `kmp-platform-sdk-core`, `kmp-platform-sdk-feature-user`, `kmp-platform-sdk-feature-clientuser`, `kmp-platform-sdk-feature-managementuser`, `kmp-platform-sdk-testing`, `kmp-platform-sdk-detekt-rules`.
- **GitHub Pages:** Dokka HTML API documentation (`./build/dokka/html`).
- **GitHub Release:** Release entry with generated release notes.
