package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email

import com.arkivanov.decompose.value.Value

/**
 * Email + password login: field updates, validation, submit, and navigation to registration or password reset.
 */
interface LoginByEmailComponent {
    /**
     * Current UI state for [LoginByEmailScreen].
     *
     * @return reactive [LoginByEmailScreenState].
     */
    val state: Value<LoginByEmailScreenState>

    /**
     * Updates the email field and recomputes email validity.
     *
     * @param email new email text.
     */
    fun onEmailChanged(email: String)

    /**
     * Updates the password field and asynchronously revalidates minimum length rules.
     *
     * @param password new password text.
     */
    fun onPasswordChanged(password: String)

    /** Toggles password field visibility. */
    fun onTogglePasswordVisibility()

    /**
     * Validates the password and runs email login; on success the parent flow completes (signed in).
     */
    fun onLoginClick()

    /** Navigates to the forgot-password / reset stack destination. */
    fun onForgotPasswordClick()

    /** Navigates to email registration. */
    fun onRegistrationClick()

    /** Pops this screen or finishes the step according to parent navigation. */
    fun onBackClick()
}