package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod

/** UI state for entering an OTP code to unlock an account. */
data class UnlockOtpScreenState(
    val method: UnlockMethod = UnlockMethod.EMAIL,
    val target: String = "",
    val codeInput: String = "",
    val remainingDelaySeconds: Int = 0,
    val actionLoading: Boolean = false,
    val actionError: AppError? = null
)
