# Authentication & Token Refresh Flow

This document describes the multi-provider authentication flows, token storage, and the transparent access token refresh mechanism provided by `kmp-platform-sdk`.

---

## 1. Authentication Overview

The SDK supports flexible multi-provider authentication:
- **Email & Password**: Direct login (`LoginByEmailUseCase`) or registration with confirmation codes (`SendRegistrationConfirmationToEmailUseCase`, `RegistrationByEmailUseCase`).
- **Phone OTP**: Login or registration via SMS verification codes (`SendLoginConfirmationToPhoneUseCase`, `LoginByPhoneUseCase`).
- **Google Sign-In**: Platform-native authentication using Jetpack Credential Manager on Android (`AndroidUserAuthServices`) and Google Identity Services JS Interop on Wasm (`WasmUserAuthServices`).
- **TOTP / MFA**: Secondary authentication step when TOTP is enabled on the user account (`LoginByTotpUseCase`, `LoginByTotpRecoveryCodeUseCase`).

---

## 2. Authentication Sequence Diagram

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Component as LoginRootComponent
    participant UseCase as LoginByEmailUseCase / LoginByPhoneUseCase
    participant Repo as LoginRepository
    participant API as KtorLoginApi
    participant AuthStore as AuthStorage (Encrypted)
    participant WS as WebSocketService

    User->>Component: Submit Credentials
    Component->>UseCase: invoke(credentials)
    UseCase->>Repo: login(credentials)
    Repo->>API: POST /auth/login
    API-->>Repo: AuthDataPayload (AccessToken, RefreshToken, UserDetails)
    Repo->>AuthStore: Save tokens (AccessToken + RefreshToken)
    Repo->>Repo: Update UserStorage cache
    Repo-->>UseCase: AppResult.Success(UserDetails)
    UseCase-->>Component: AppResult.Success(UserDetails)
    Component->>WS: Re-connect WebSocket with new AccessToken
    Component-->>User: Navigate to Main Application Screen
```

---

## 3. Synchronized Token Refresh Flow (`AuthHttpClientConfigPlugin`)

When an HTTP API request fails with `401 Unauthorized`, `AuthHttpClientConfigPlugin` handles token renewal transparently without interrupting the user experience.

```mermaid
sequenceDiagram
    autonumber
    participant API as Ktor Client / Remote Server
    participant Plugin as AuthHttpClientConfigPlugin
    participant Mutex as Refresh Mutex
    participant RefreshUC as RefreshTokenUseCase
    participant AuthStore as AuthStorage

    API-->>Plugin: HTTP 401 Unauthorized
    Plugin->>Mutex: Acquire lock (prevents concurrent refresh calls)
    
    alt Token already refreshed by another concurrent request
        Plugin->>AuthStore: Get updated AccessToken
    else First thread to enter refresh block
        Plugin->>RefreshUC: invoke()
        RefreshUC->>API: POST /auth/refresh (RefreshToken)
        
        alt Refresh Succeeded
            API-->>RefreshUC: New AccessToken + RefreshToken
            RefreshUC->>AuthStore: Save new tokens
        else Refresh Failed (Invalid or Expired RefreshToken)
            API-->>RefreshUC: HTTP 400 / 401 Error
            RefreshUC->>AuthStore: Clear tokens
            RefreshUC-->>Plugin: Return InvalidRefreshToken error
            Plugin-->>API: Emit session expired event (Redirect to Login)
        end
    end
    
    Plugin->>Mutex: Release lock
    Plugin->>API: Retry original request with new AccessToken
    API-->>Plugin: HTTP 200 OK Response
```

---

## 4. Multi-Platform Social Auth Implementations

- **Android (`AndroidUserAuthServices`)**: Interacts with `CredentialManager` to request Google ID tokens securely from the Android system credential provider.
- **Wasm (`WasmUserAuthServices`)**: Uses Kotlin/JS interop to render the Google Sign-In button and handle ID token callbacks from Google Identity Services in the browser viewport.
