# Security & Password Policy / TOTP / Unlock Flows

This document details password policy validation, Time-based One-Time Password (TOTP) MFA lifecycle, and account recovery flows within `kmp-platform-sdk`.

---

## 1. Backend-Driven Password Policy Validation

The SDK enforces configurable password rules supplied dynamically by the backend via `core:security`:

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant UI as Password Input Component
    participant UC as ValidatePasswordUseCase
    participant Validator as PasswordPolicyValidator
    participant Repo as SecuritySettingsRepository
    participant Storage as EncryptedSecuritySettingsStorage

    UI->>UC: invoke("CandidatePassword123!")
    UC->>Repo: getSecuritySettings()
    Repo->>Storage: Read cached PasswordPolicy
    Storage-->>Repo: PasswordPolicy(minLength=8, requireLetter=true, requireDigit=true, requireSpecialChar=true)
    Repo-->>UC: PasswordPolicy
    
    UC->>Validator: validate("CandidatePassword123!", policy)
    
    alt Policy Passed
        Validator-->>UC: AppResult.Success(Unit)
        UC-->>UI: Validation Succeeded
    else Policy Failed
        Validator-->>UC: AppResult.Error(SecurityError.PasswordTooShort / PasswordNoSpecialChar)
        UC-->>UI: Display specific SecurityError message
    end
```

---

## 2. TOTP MFA Lifecycle Flow

Users manage Two-Factor Authentication via `TotpMainComponent` in `feature/user`:

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Component as TotpMainComponent
    participant UC as SetupTotpUseCase / EnableTotpUseCase
    participant Repo as UserSecurityRepository
    participant API as UserSecurityApi

    User->>Component: Click "Enable 2FA"
    Component->>UC: SetupTotpUseCase()
    UC->>Repo: setupTotp()
    Repo->>API: POST /user/security/totp/setup
    API-->>Repo: TotpSetupData (SecretKey, QrCodeUrl)
    Repo-->>Component: Display QR Code & Manual Secret Key
    
    User->>Component: Enter 6-digit TOTP code from Authenticator App
    Component->>UC: EnableTotpUseCase(totpCode)
    UC->>Repo: enableTotp(totpCode)
    Repo->>API: POST /user/security/totp/enable
    API-->>Repo: List of Emergency Recovery Codes
    Repo-->>Component: Display Recovery Codes (Prompt user to save)
```

---

## 3. Account Unlock Flow (`SECURITY_HOLD` Recovery)

When an account enters `SECURITY_HOLD` due to failed attempt thresholds or suspicious logins, standard authentication is blocked until the recovery flow is completed:

1. **Detection**: `LoginByEmailUseCase` or `LoginByPhoneUseCase` receives `UserAccountStatus.SECURITY_HOLD`.
2. **Navigation**: Decompose router navigates to `UnlockRootComponent`.
3. **Target Selection**: User chooses recovery delivery method (Email OTP or Phone SMS OTP) via `UnlockMethodSelectionComponent`.
4. **Verification**: User enters OTP code received via chosen delivery method.
5. **Account Restoration**: Upon successful OTP submission, account status transitions back to `ACTIVE`, issuing a fresh session token pair.
