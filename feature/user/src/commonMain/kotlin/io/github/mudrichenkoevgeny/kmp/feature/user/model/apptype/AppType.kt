package io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype

/**
 * Defines the operational context of the application.
 *
 * Used to conditionally enable or disable features, UI components, or navigation paths
 * based on whether the application is intended for end-users or internal management staff.
 */
enum class AppType {
    /**
     * Standard client-facing application (e.g., mobile user app).
     * Enables all public features such as self-registration and public authentication flows.
     */
    CLIENT,

    /**
     * Internal management or administrative application.
     * Restricts access to public features like self-registration to enforce administrative policies.
     */
    MANAGEMENT
}