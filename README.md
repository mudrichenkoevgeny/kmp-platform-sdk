# kmp-platform-sdk

A modular **Kotlin Multiplatform** client SDK for Android and Web (Wasm). It bundles a shared **Ktor HTTP client**, **WebSockets**, **encrypted storage** (Android-first), **settings / security / user** domains, **Compose Multiplatform** UI for auth flows, and **Decompose** navigation—so host apps can wire runtime config once and reuse the same stack across targets.

Targets today are primarily **Android** and **Wasm**; iOS source sets exist as scaffolding where noted in Gradle.

## Modules

| Module | Purpose |
|--------|--------|
| **core/common** | Base for all SDK modules: Ktor client bootstrap, WebSocket service, `EncryptedSettings`, device/platform metadata, `AppError` parsing pipeline, shared Compose building blocks, mocks. Does not depend on other in-repo `core/*` or `feature/*` modules. |
| **core/settings** | Global settings: storage, HTTP API, repository, refresh use cases, WebSocket message handler. |
| **core/security** | Security settings, password policy validation (via shared foundation), refresh use cases, WebSocket handler, `SecurityErrorParser`. |
| **feature/user** | Auth (email, phone, Google), tokens, auth settings, user profile cache, `AuthHttpClientConfigPlugin`, `UserWebSocketMessageHandler`, `UserErrorParser`, Decompose login/registration UI. Depends on **core/common**, **core/settings**, **core/security**. |
| **bom** | Java platform module: Gradle constraints so `core/*` and `feature/*` stay on aligned versions when consumed together. |
| **sample** | Reference **composeApp** (shared UI + Wasm + Android library target) and **androidApp** showing how to build `AppComponent`, call `init()`, register parsers and WebSocket handlers, and host `RootContent`. |

Depend only on what you need. Per-module details:

- [core/common/README.md](core/common/README.md)
- [core/settings/README.md](core/settings/README.md)
- [core/security/README.md](core/security/README.md)
- [feature/user/README.md](feature/user/README.md)
- [sample/README.md](sample/README.md)

## Adding the SDK to your project

Consume the SDK **from source**: include this repository in your Gradle build (monorepo layout, Git submodule, or `includeBuild`), then add **project** dependencies from your Kotlin Multiplatform targets.

Example (your app lives in the same composite build and can see these projects):

```kotlin
// build.gradle.kts — commonMain (or androidMain / wasmJsMain as needed)
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:common"))
            implementation(project(":core:settings"))
            implementation(project(":core:security"))
            implementation(project(":feature:user"))
        }
    }
}
```

Adjust project paths (`:core:common`, etc.) to match how you **include** this repo in `settings.gradle.kts`. To keep module versions consistent inside one composite build, you can depend on **`project(":bom")`** as a platform alongside the feature and core projects.

Shared **foundation** types and contracts are pulled in via dependencies declared in this repo’s `gradle/libs.versions.toml` (your app inherits them transitively when you depend on the SDK modules).

## Integration steps

1. **Storage** — Provide platform `EncryptedSettings` by constructing [EncryptedSettingsComponent](core/common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/EncryptedSettingsComponent.kt) (or an equivalent host-owned setup) and reuse that instance everywhere below.

2. **Common** — Build [CommonComponent](core/common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/CommonComponent.kt) with `baseUrl`, `deviceInfo`, `encryptedSettings`, optional `platformContext`, `accessTokenProvider`, and `httpClientConfigPlugins` (include [AuthHttpClientConfigPlugin](feature/user/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/network/httpclient/AuthHttpClientConfigPlugin.kt) from **feature/user** when you use auth APIs).

3. **Settings & security** — Construct [SettingsComponent](core/settings/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/di/SettingsComponent.kt) and [SecurityComponent](core/security/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/di/SecurityComponent.kt) with the **same** `encryptedSettings`, Ktor `HttpClient`, and `WebSocketService` as `CommonComponent`.

4. **User** — Build [UserComponent](feature/user/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/di/UserComponent.kt) with `commonComponent`, `settingsComponent`, `securityComponent`, [AuthStorage](feature/user/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/storage/auth/AuthStorage.kt) (e.g. [EncryptedAuthStorage](feature/user/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/storage/auth/EncryptedAuthStorage.kt)), and platform [UserAuthServices](feature/user/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/UserAuthServices.kt).

5. **Init** — Call `CommonComponent.init(...)` with feature parsers (`SecurityErrorParser`, `UserErrorParser`, …), then `webSocketService.updateWebSocketMessageHandlers(...)` with the combined list (common, settings, security, user). Connect the socket when your app is ready.

6. **UI** — Use `CompositionLocalProvider` with `LocalCommonComponent` / `LocalErrorParser` (see [RootContent](sample/composeApp/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/sample/app/ui/root/RootContent.kt) in the sample) and embed feature UI such as `UserComponent.createLoginRootDialogComponent(...)`.

For a full wiring example, see the [sample](sample) application and [sample/README.md](sample/README.md).
