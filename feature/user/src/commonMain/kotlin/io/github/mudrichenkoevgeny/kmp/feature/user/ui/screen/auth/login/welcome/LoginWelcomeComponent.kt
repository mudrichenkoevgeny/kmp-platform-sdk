package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome

import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider

/**
 * Presentation logic for the welcome step: loads providers and legal URLs, handles provider selection and OAuth.
 */
interface LoginWelcomeComponent {
    /**
     * Current screen state for [LoginWelcomeScreen].
     *
     * @return reactive [LoginWelcomeScreenState].
     */
    val state: Value<LoginWelcomeScreenState>

    /**
     * Retries loading available providers and global settings after [LoginWelcomeScreenState.InitializationError].
     */
    fun onRetryInitClick()

    /**
     * Handles the user’s choice of sign-in method: navigates to email/phone flows or starts OAuth where supported.
     *
     * @param authProvider selected provider.
     */
    fun onLoginClick(authProvider: UserAuthProvider)

    /**
     * Opens the privacy policy URL from the loaded global settings when available.
     */
    fun onPrivacyPolicyClick()

    /**
     * Opens the terms of service URL from the loaded global settings when available.
     */
    fun onTermsOfServiceClick()
}