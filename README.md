# kmp-platform-sdk

A modular **Kotlin Multiplatform (KMP)** client SDK for Android and Web (Wasm). It provides a unified foundation for building multiplatform applications with shared logic for networking, encrypted storage, security policies, and identity management. By bundling **Compose Multiplatform** UI and **Decompose** navigation, it allows host apps to integrate complex auth flows and system settings with minimal boilerplate.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.mudrichenkoevgeny/kmp-platform-sdk-bom)](https://central.sonatype.com/artifact/io.github.mudrichenkoevgeny/kmp-platform-sdk-bom)

## Modules

| Module | Purpose |
| :--- | :--- |
| **core/common** | **Foundation:** Ktor bootstrap, WebSocket lifecycle, `EncryptedSettings` abstraction, platform metadata, and error parsing. |
| **core/settings** | **Global Settings:** Logic for application configuration, encrypted caching, and reactive state management. |
| **core/security** | **Security Domain:** Password policy validation, MFA state management, and localized security errors. |
| **feature/settingsapi** | **Settings Network:** Ktor implementation for fetching global application configurations. |
| **feature/securityapi** | **Security Network:** Ktor implementation for fetching security policies and MFA requirements. |
| **feature/user** | **Base Identity:** Foundational models, use cases, and storage for user identity and authentication. |
| **feature/clientuser** | **Client Identity:** Identity solution for standard user applications, including UI and social login. |
| **feature/managementuser** | **Management Identity:** Administrative identity solution for internal staff and resource oversight. |
| **bom** | **Bill of Materials:** Gradle platform to ensure version alignment across all SDK modules. |

## Installation

Add the BOM and the required modules to your `commonMain` dependencies:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(platform("io.github.mudrichenkoevgeny:kmp-platform-sdk-bom:0.0.1"))
            implementation("io.github.mudrichenkoevgeny:kmp-platform-sdk-core-common")
            implementation("io.github.mudrichenkoevgeny:kmp-platform-sdk-feature-clientuser")
            // Add other core or feature modules as needed
        }
    }
}
```

## Integration Steps

### 1. Storage & Infrastructure
Initialize the `EncryptedSettingsComponent` and the root `CommonComponent` using platform-specific context.

```kotlin
val encryptedSettingsComponent = EncryptedSettingsComponent(platformContext)

val commonComponent = CommonComponent(
    encryptedSettings = encryptedSettingsComponent.encryptedSettings,
    deviceInfo = deviceInfo,
    baseUrl = "https://api.example.com",
    accessTokenProvider = authStorage,
    appScope = appScope,
    platformContext = platformContext
)
```

### 2. Feature API & Components
Construct the networking providers and domain components by sharing the core `HttpClient` and `WebSocketService`.

```kotlin
val securityApi = SecurityApiComponent(httpClient = commonComponent.httpClient).securitySettingsApi
val settingsApi = SettingsApiComponent(httpClient = commonComponent.httpClient).globalSettingsApi

val securityComponent = SecurityComponent(
    webSocketService = commonComponent.webSocketService,
    securitySettingsApi = securityApi,
    encryptedSettings = commonComponent.encryptedSettings,
    parentScope = appScope
)

val settingsComponent = SettingsComponent(
    webSocketService = commonComponent.webSocketService,
    globalSettingsApi = settingsApi,
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

For a complete wiring example, refer to the [sampleclient](sampleclient) or [samplemanagement](samplemanagement) applications.
