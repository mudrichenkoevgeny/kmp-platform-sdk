package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.unlock

import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod
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
        val method: UnlockMethod
    ) : UnlockDestination

    /** OTP confirmation code entry for email or phone unlock. */
    @Serializable data class OtpInput(
        val method: UnlockMethod,
        val target: String
    ) : UnlockDestination

    /** Success confirmation screen. */
    @Serializable object Success : UnlockDestination
}
