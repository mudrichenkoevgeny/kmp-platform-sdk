---
description: Internal bootstrapping, AppError pipeline, listing infrastructure, and specific deep link routing for kmp-platform-sdk
globs: "**/*.kt"
alwaysApply: true
---

# Architecture and Design Patterns — kmp-platform-sdk

This document defines internal wiring, paging conventions, and deep linking rules specific to `kmp-platform-sdk`.

## 1. SDK Bootstrap & Host Wiring
- **Initialization Sequence:**
  1. Instantiate `CommonComponent` providing `baseUrl` and `platformContext`.
  2. Initialize domain components (e.g., `ClientUserComponent`) passing `CommonComponent` and parent scopes.
  3. Invoke `commonComponent.init()` supplying all feature-specific `AppErrorParser` implementations.
- **WebSocket Handlers:** Feature modules implement `WebSocketMessageHandler` and register instances into `commonComponent.webSocketService`.

## 2. Listing & Pagination Infrastructure
All paged lists in this SDK must utilize the standardized primitives from `core:common`:
- **State Container:** Use `PaginationState<T>` from `io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing`.
- **State Transitions:** Utilize `appendResult()`, `toNextPageLoading()`, and `toError()`.
- **UI Trigger & Feedback:** Detect scrolling via `LazyListState.OnBottomReached` and always append `PagingFooter` at the end of the `LazyColumn`.

## 3. Storage & Error Integration
- **Encrypted Storage:** Use `EncryptedSettings` from `core/common`. Never access platform storage APIs directly in feature modules.
- **Error Pipeline:** All domain errors extend `AppError`, pass through `AppErrorParser`, and resolve to localized resources via `LocalErrorParser.current`.

## 4. App-Specific Deep Link Aggregators
- **App Aggregators:** Root aggregators (`ManagementAppDeepLinkParser`, `ClientAppDeepLinkParser`) and sealed route models (`ManagementAppDeepLinkRoute`, `ClientAppDeepLinkRoute`) reside in application modules (`samplemanagement` / `sampleclient`) to maintain strict dependency graph isolation.
