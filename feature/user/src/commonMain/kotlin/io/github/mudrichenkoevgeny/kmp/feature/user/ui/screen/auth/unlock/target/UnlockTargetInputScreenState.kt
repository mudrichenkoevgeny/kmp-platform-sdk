package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator

/** UI state for the intermediate unlock input step (entering email or phone). */
data class UnlockTargetInputScreenState(
    val method: UnlockMethod,
    val input: String = "",
    val actionLoading: Boolean = false,
    val actionError: AppError? = null
) {
    val isInputValid: Boolean
        get() = when (method) {
            UnlockMethod.EMAIL -> FieldValidator.isEmailValid(input.trim())
            UnlockMethod.PHONE -> FieldValidator.isPhoneNumberValid(input.trim())
        }

    val canSendCode: Boolean
        get() = isInputValid && !actionLoading
}
