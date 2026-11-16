package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value

/**
 * Phone-based login: request SMS code, enter code, optional timer and change-phone actions.
 */
public interface LoginByPhoneComponent {
    /**
     * Decompose navigation stack driving the phone login wizard steps.
     */
    public val stack: Value<ChildStack<LoginByPhoneStepConfig, Child>>

    /**
     * Stack navigator for the phone login wizard.
     */
    public val stackNavigation: StackNavigation<LoginByPhoneStepConfig>

    /**
     * Current UI state for [LoginByPhoneScreen].
     *
     * @return reactive [LoginByPhoneScreenState].
     */
    public val state: Value<LoginByPhoneScreenState>

    /**
     * Child instances corresponding to each wizard step.
     */
    public sealed interface Child {
        /** Phone input wizard step. */
        public data class PhoneInput(public val initialPhone: String) : Child

        /** SMS code confirmation step. */
        public data class CodeInput(public val phone: String) : Child
    }

    /**
     * Updates the phone field; may auto-advance to code entry if a cooldown still applies for this number.
     *
     * @param phone new phone text.
     */
    public fun onPhoneChanged(phone: String)

    /**
     * Updates the SMS code; may auto-submit when the code reaches the configured length.
     *
     * @param code new code text.
     */
    public fun onCodeChanged(code: String)

    /** Requests an SMS confirmation code for the current phone number. */
    public fun onSendCodeClick()

    /** Returns from code entry to phone entry, keeping the typed phone number. */
    public fun onResetPhoneClick()

    /** Submits phone and code to complete sign-in. */
    public fun onConfirmCodeClick()

    /**
     * Back from phone step pops the screen; from code step returns to phone input ([onResetPhoneClick]).
     */
    public fun onBackClick()
}
