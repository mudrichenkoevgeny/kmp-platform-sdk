package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp

import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod
import kotlinx.serialization.Serializable

/**
 * Navigation arguments for unlock OTP confirmation screen.
 */
@Serializable
data class UnlockOtpParams(
    val method: UnlockMethod,
    val target: String,
    val initialDelaySeconds: Int = 0
)
