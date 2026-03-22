package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.phone

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError

/**
 * UI state for phone login: enter phone and send code, then enter SMS code with optional resend timer.
 */
sealed interface LoginByPhoneScreenState {

    /** Reserved loading shell; the implementation currently starts in [PhoneInput]. */
    data object Loading : LoginByPhoneScreenState

    /**
     * User enters a phone number and requests an SMS code.
     *
     * @param phoneNumber current phone text.
     * @param isPhoneNumberValid whether the number passes client-side validation.
     * @param actionLoading while sending the code request.
     * @param actionError error from send-code or rate limiting.
     */
    data class PhoneInput(
        val phoneNumber: String = "",
        val isPhoneNumberValid: Boolean = false,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : LoginByPhoneScreenState

    /**
     * User enters the SMS code; optional countdown before resend is allowed.
     *
     * @param phoneNumber phone number the code was sent to (for display).
     * @param code current confirmation code input.
     * @param codeLength expected code length (default six digits).
     * @param resendTimerSeconds seconds remaining before resend is allowed; zero means resend is available.
     * @param actionLoading while confirming login.
     * @param actionError error from confirm or related steps.
     */
    data class CodeInput(
        val phoneNumber: String,
        val code: String = "",
        val codeLength: Int = 6,
        val resendTimerSeconds: Int = 0,
        val actionLoading: Boolean = false,
        val actionError: AppError? = null
    ) : LoginByPhoneScreenState {
        val isCodeFullLength: Boolean = code.length == codeLength
        val canResendCode: Boolean = resendTimerSeconds == 0 && !actionLoading
    }
}