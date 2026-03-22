package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.phone

import com.arkivanov.decompose.value.Value

/**
 * Phone-based login: request SMS code, enter code, optional timer and change-phone actions.
 */
interface LoginByPhoneComponent {
    /**
     * Current UI state for [LoginByPhoneScreen].
     *
     * @return reactive [LoginByPhoneScreenState].
     */
    val state: Value<LoginByPhoneScreenState>

    /**
     * Updates the phone field; may auto-advance to code entry if a cooldown still applies for this number.
     *
     * @param phone new phone text.
     */
    fun onPhoneChanged(phone: String)

    /**
     * Updates the SMS code; may auto-submit when the code reaches the configured length.
     *
     * @param code new code text.
     */
    fun onCodeChanged(code: String)

    /** Requests an SMS confirmation code for the current phone number. */
    fun onSendCodeClick()

    /** Returns from code entry to phone entry, keeping the typed phone number. */
    fun onResetPhoneClick()

    /** Submits phone and code to complete sign-in. */
    fun onConfirmCodeClick()

    /**
     * Back from phone step pops the screen; from code step returns to phone input ([onResetPhoneClick]).
     */
    fun onBackClick()
}