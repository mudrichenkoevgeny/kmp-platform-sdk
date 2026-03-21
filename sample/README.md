# sample

Reference host applications that show how to wire **kmp-platform-sdk** modules for Android and Wasm (browser).
They are not published artifacts: copy the wiring pattern into your own app.

## Modules

- **`sample:composeApp`** — Kotlin Multiplatform library with shared Compose UI, [AppComponent], Decompose screens, and platform entry points (`wasmJs`, `android` as a library target for embedding).
- **`sample:androidApp`** — Android application that depends on `composeApp` and hosts [MainActivity].

## What it provides

- **Root wiring:** [AppComponent] builds [EncryptedSettingsComponent] (via common), [CommonComponent], [SettingsComponent], [SecurityComponent], and [UserComponent]; shares one `EncryptedSettings`, Ktor `HttpClient`, and `WebSocketService` across modules.
- **Initialization:** [AppComponent.init] registers `SecurityErrorParser` and `UserErrorParser` on the common error pipeline and installs the combined WebSocket handler list (`common`, settings, security, user).
- **Startup data:** optional parallel refresh via [SyncDataUseCase] ([AppUseCaseModule]); Android and Wasm call `refreshUserConfigurationUseCase()` and open the socket after `init()`.
- **UI shell:** [RootContent] shows a splash until initialization completes, then provides `LocalCommonComponent`, `LocalErrorParser`, and `LocalAppComponent` and hosts [MainScreen].
- **Android host:** [AndroidApp] creates [AppComponent] with `BuildConfig.BASE_URL`, [AndroidDeviceInfoProvider], and [AndroidUserAuthServices]; [MainActivity] sets Compose content to [RootContent].
- **Wasm host:** [main] (Wasm) builds [AppComponent] with [WasmDeviceInfoProvider], [WasmUserAuthServices], and generated `BuildConfig`; connects WebSockets then runs [RootContent] in a browser viewport.
- **Tests / previews:** [AppComponentMock] supplies mocked SDK components for UI tests and tooling.

iOS targets are commented out in `composeApp` Gradle config; [MainViewController] under `iosMain` is a scaffold for future enablement.

## Configuration

`composeApp` uses the BuildConfig plugin. Fields such as `BASE_URL`, `GOOGLE_WEB_CLIENT_ID`, and `APP_VERSION` are defined in [composeApp build.gradle.kts] (`app.env` selects `dev` / `test` / `prod` blocks). Adjust values for your backend and OAuth client IDs.

The Android application module does not duplicate that config; it consumes `composeApp` and uses the same generated `io.github.mudrichenkoevgeny.kmp.sample.BuildConfig` types where referenced from shared code.

## Usage

1. Open the root Gradle project and run the **`sample:androidApp`** configuration, or the Wasm/browser target for **`sample:composeApp`**, depending on the platform you want.
2. For your own app, mirror [AppComponent]: construct common + feature components with shared storage and network objects, call `init()`, register parsers and WebSocket handlers the same way, then start your UI under `CompositionLocalProvider` like [RootContent].

## Related module docs

- [core/common](../core/common/README.md) — HTTP client, WebSockets, errors, platform storage.
- [core/settings](../core/settings/README.md) — global settings.
- [core/security](../core/security/README.md) — security settings.
- [feature/user](../feature/user/README.md) — auth, auth settings, user profile, HTTP auth plugin, user WebSocket handler, login UI.

[AppComponent]: composeApp/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/sample/app/di/AppComponent.kt
[EncryptedSettingsComponent]: ../core/common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/EncryptedSettingsComponent.kt
[CommonComponent]: ../core/common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/CommonComponent.kt
[SettingsComponent]: ../core/settings/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/di/SettingsComponent.kt
[SecurityComponent]: ../core/security/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/di/SecurityComponent.kt
[UserComponent]: ../feature/user/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/di/UserComponent.kt

[AppComponent.init]: composeApp/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/sample/app/di/AppComponent.kt
[SyncDataUseCase]: composeApp/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/sample/app/usecase/SyncDataUseCase.kt
[AppUseCaseModule]: composeApp/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/sample/app/di/AppUseCaseModule.kt

[RootContent]: composeApp/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/sample/app/ui/root/RootContent.kt
[MainScreen]: composeApp/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/sample/app/ui/screen/main/MainScreen.kt

[AndroidApp]: androidApp/src/main/kotlin/io/github/mudrichenkoevgeny/kmp/sample/android/AndroidApp.kt
[MainActivity]: androidApp/src/main/kotlin/io/github/mudrichenkoevgeny/kmp/sample/android/MainActivity.kt
[AndroidDeviceInfoProvider]: ../core/common/src/androidMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/platform/deviceinfo/AndroidDeviceInfoProvider.kt
[AndroidUserAuthServices]: ../feature/user/src/androidMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/AndroidUserAuthServices.kt

[main]: composeApp/src/wasmJsMain/kotlin/io/github/mudrichenkoevgeny/kmp/sample/app/main.kt
[WasmDeviceInfoProvider]: ../core/common/src/wasmJsMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/platform/deviceinfo/WasmDeviceInfoProvider.kt
[WasmUserAuthServices]: ../feature/user/src/wasmJsMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/WasmUserAuthServices.kt

[AppComponentMock]: composeApp/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/sample/app/mock/di/AppComponentMock.kt
[MainViewController]: composeApp/src/iosMain/kotlin/io/github/mudrichenkoevgeny/kmp/sample/app/MainViewController.kt

[composeApp build.gradle.kts]: composeApp/build.gradle.kts
