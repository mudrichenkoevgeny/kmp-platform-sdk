---
description: Error resource key conventions and AppError alignment for kmp-platform-sdk
globs: "**/composeResources/**/*.xml"
alwaysApply: true
---

# Error Localization Conventions — kmp-platform-sdk

This document defines error string conventions aligned with domain models in the `kmp-platform-sdk` repository.

## 1. Domain Error Key Conventions
Resource keys for errors must strictly map to `AppError` code hierarchies and their corresponding `AppErrorParser` implementations:
- Common Platform Errors: `error_common_*`
- User & Auth Errors: `error_user_*`
- Security & MFA Errors: `error_security_*`
- Settings Errors: `error_settings_*`

## 2. Placeholder Alignment
- XML placeholders (`%1$s`, `%d`) must strictly match the positional arguments passed via `AppError.args`.
- Unmapped or unknown error codes must always fall back gracefully to the default message handled by `CommonErrorParser`.
