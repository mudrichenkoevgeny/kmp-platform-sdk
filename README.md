# kmp-platform-sdk

A modular **Kotlin Multiplatform (KMP)** client SDK for Android and Web (Wasm). It provides a unified foundation for building multiplatform applications with shared logic for networking, encrypted storage, security policies, and identity management. By bundling **Compose Multiplatform** UI and **Decompose** navigation, it allows host apps to integrate complex auth flows and system settings with minimal boilerplate.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.mudrichenkoevgeny/kmp-platform-sdk-bom)](https://central.sonatype.com/artifact/io.github.mudrichenkoevgeny/kmp-platform-sdk-bom)

## Installation

Add the BOM and the required modules to your `commonMain` dependencies:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(platform("io.github.mudrichenkoevgeny:kmp-platform-sdk-bom:<version>"))
            implementation("io.github.mudrichenkoevgeny:kmp-platform-sdk-core")
            implementation("io.github.mudrichenkoevgeny:kmp-platform-sdk-feature-clientuser")
            // Add other kmp-platform-sdk modules as required
        }
    }
}
```

With a Version Catalog (`gradle/libs.versions.toml`), declare the BOM and module aliases, then use `implementation(platform(libs.kmp.platform.sdk.bom))` and `implementation(libs.kmp.platform.sdk.core)`.

## Modules

Published artifacts (versions aligned via the BOM):

- **core** — Consolidated foundation for all SDK modules: Ktor HTTP client bootstrap, WebSocket lifecycle management, `EncryptedSettings` abstraction, global settings, password policy & MFA, file storage infrastructure, error modeling, listing/pagination, and deep link navigation contracts ([module README](core/README.md)).
- **feature-user** — Foundational identity & auth domain logic: core models, use cases, token storage (`AuthStorage`), session auto-refresh, TOTP 2FA, session management, identifier linking, and account deletion ([module README](feature/user/README.md)).
- **feature-clientuser** — Identity solution for consumer applications: multi-method auth (Email, Phone OTP, Google Sign-In), Compose Multiplatform UI, social login buttons, and Decompose navigation flows ([module README](feature/clientuser/README.md)).
- **feature-managementuser** — Administrative identity solution for internal staff, resource oversight, administrative user management, and audit inspection ([module README](feature/managementuser/README.md)).
- **bom** — Dependency constraints for version alignment across all modules above.

## Documentation & Architecture

- **[ARCHITECTURE.md](docs/architecture/ARCHITECTURE.md)** — High-level architecture, module dependency graph, domain models (`AppType`, `UserRole`, `UserAccountStatus` state machine, `UserSession`), universal deep linking architecture, `*Params` screen arguments, and detailed sequence diagrams for runtime flows (SDK bootstrap, token refresh, error parsing, WebSockets, TOTP/security, account management).
- **[CONTRIBUTING.md](CONTRIBUTING.md)** — Build environment configurations, dependency analysis, Roborazzi screenshot testing, and Maven Central publishing guidelines.
- **[AGENTS.MD](AGENTS.MD)** — Project standards, module boundaries, coding style, and architectural rules for contributors and agent coding assistants.

## Design Tokens & Theme Integration

Visual design decisions across the SDK are driven by design tokens:

1. **Source of Truth**: All design tokens, font assets, and vector icon drawables originate from `shared-foundation` and are generated/extracted automatically at build time via the `shared-foundation-design-system` Gradle plugin.
2. **Generated Tokens**: Design token sources (`PlatformDesignTokens.kt`) are placed automatically into `build/generated/sources/designTokens/...` during build.
3. **Font & Icon Assets**: Custom font files (`pt_sans_*.ttf`) and vector drawables are extracted directly into Compose Multiplatform resources during build per module's `iconModules` configuration.
4. **Theme Integration**: `CoreTheme` maps colors (`Color.kt`), dimensions (`CoreDimens.kt`), shapes (`Shapes.kt`), and typography (`Typography.kt`) directly from generated `PlatformDesignTokens`.
5. **Font & Drawable Resources**: `Typography.kt` loads custom fonts via Compose Multiplatform resources (`Res.font.*`), and components consume extracted icons via Compose resources (`Res.drawable.*`).

## Integration Steps

### 1. Storage & Infrastructure
Initialize the `EncryptedSettingsComponent` and the root `CommonComponent` using platform-specific context.

```kotlin
val encryptedSettingsComponent = EncryptedSettingsComponent(platformContext)

val commonComponent = CommonComponent(
    encryptedSettings = encryptedSettingsComponent.encryptedSettings,
    clientDeviceInfo = clientDeviceInfo,
    baseUrl = "https://api.example.com",
    accessTokenProvider = authStorage,
    appScope = appScope,
    platformContext = platformContext
)
```

### 2. Feature Components
Construct domain components by sharing the core `HttpClient` and `WebSocketService`.

```kotlin
val securityComponent = SecurityComponent(
    webSocketService = commonComponent.webSocketService,
    httpClient = commonComponent.httpClient,
    encryptedSettings = commonComponent.encryptedSettings,
    parentScope = appScope
)

val settingsComponent = SettingsComponent(
    webSocketService = commonComponent.webSocketService,
    httpClient = commonComponent.httpClient,
    encryptedSettings = commonComponent.encryptedSettings,
    parentScope = appScope
)
```

### 3. Client User Identity Setup
Wire the `ClientUserComponent` with its core collaborators and platform-specific authentication services.

```kotlin
val clientUserComponent = ClientUserComponent(
    commonComponent = commonComponent,
    settingsComponent = settingsComponent,
    securityComponent = securityComponent,
    authStorage = encryptedAuthStorage,
    authServices = platformAuthServices,
    parentScope = appScope
)
```

### 4. System Initialization
Register auth interceptors, domain error parsers, and WebSocket message handlers during the application startup sequence.

```kotlin
fun init() {
    commonComponent.httpClientConfigPlugins.add(clientUserComponent.authHttpClientConfigPlugin)

    commonComponent.init(
        appErrorParserSpecificParsers = listOf(
            SecurityErrorParser,
            UserErrorParser
        )
    )

    commonComponent.webSocketService.updateWebSocketMessageHandlers(
        listOf(
            commonComponent.commonWebSocketMessageHandler,
            settingsComponent.settingsWebSocketMessageHandler,
            securityComponent.securityWebSocketMessageHandler,
            clientUserComponent.userWebSocketMessageHandler
        )
    )
}
```

### 5. UI Integration
Inject the SDK graph into your Compose Multiplatform tree using `CompositionLocalProvider`.

```kotlin
@Composable
fun App(clientAppComponent: ClientAppComponent) {
    val isInitialized by clientAppComponent.isInitialized.collectAsState()

    if (isInitialized) {
        CompositionLocalProvider(
            LocalCommonComponent provides clientAppComponent.commonComponent,
            LocalErrorParser provides clientAppComponent.commonComponent.appErrorParser,
            LocalClientAppComponent provides clientAppComponent
        ) {
            // Embed feature UI or launch LoginRootComponent navigation
        }
    } else {
        SplashScreen()
    }
}
```

For full wiring examples, refer to the **[sampleclient](sampleclient)** and **[samplemanagement](samplemanagement)** reference applications.

## License

This project is licensed under the Apache License 2.0 — see the [LICENSE](LICENSE) file for details.
