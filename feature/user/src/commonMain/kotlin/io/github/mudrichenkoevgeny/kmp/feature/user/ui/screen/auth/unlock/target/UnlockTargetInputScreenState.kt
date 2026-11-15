package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.feature.user.utils.FieldValidator
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.unlock.UnlockMethod

/** UI state for the intermediate unlock input step (entering email or phone). */
public data class UnlockTargetInputScreenState(
    val method: UnlockMethod,
    val input: String = "",
    val actionLoading: Boolean = false,
    val actionError: AppError? = null
) {
    public val isInputValid: Boolean
        get() = when (method) {
            UnlockMethod.EMAIL -> FieldValidator.isEmailValid(input.trim())
            UnlockMethod.PHONE -> FieldValidator.isPhoneNumberValid(input.trim())
        }

    public val canSendCode: Boolean
        get() = isInputValid && !actionLoading
}
