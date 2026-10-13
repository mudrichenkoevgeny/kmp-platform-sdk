package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock

import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp.UnlockOtpParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target.UnlockTargetInputParams
import kotlinx.serialization.Serializable

/**
 * Serializable configurations for the account unlock navigation stack router.
 */
@Serializable
sealed interface UnlockDestination {
    /** Selection of unlock method (Email OTP, SMS OTP, OAuth Google / Apple). */
    @Serializable object MethodSelection : UnlockDestination

    /** Intermediate target input step (entering email address or phone number). */
    @Serializable data class TargetInput(
        val params: UnlockTargetInputParams
    ) : UnlockDestination {
        val method: UnlockMethod get() = params.method
    }

    /** OTP confirmation code entry for email or phone unlock. */
    @Serializable data class OtpInput(
        val params: UnlockOtpParams
    ) : UnlockDestination {
        val method: UnlockMethod get() = params.method
        val target: String get() = params.target
        val initialDelaySeconds: Int get() = params.initialDelaySeconds
    }

    /** Success confirmation screen. */
    @Serializable object Success : UnlockDestination
}
