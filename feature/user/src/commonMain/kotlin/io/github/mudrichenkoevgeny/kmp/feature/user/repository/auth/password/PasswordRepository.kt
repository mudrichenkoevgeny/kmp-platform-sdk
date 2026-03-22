package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.password

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier.UserIdentifier

/**
 * Password recovery: reset with a confirmation code and send-code flows with client-side retry hints.
 */
interface PasswordRepository {
    /**
     * Completes password reset for the account identified by [email] using [newPassword] and
     * [confirmationCode] from the email confirmation step.
     *
     * @param email Account email used in the reset flow.
     * @param newPassword Replacement password after successful verification.
     * @param confirmationCode One-time code delivered to [email].
     * @return [UserIdentifier] on success, or an error [AppResult] on failure.
     */
    suspend fun resetPassword(
        email: String,
        newPassword: String,
        confirmationCode: String
    ): AppResult<UserIdentifier>

    /**
     * Requests a password-reset confirmation code to be sent to [email].
     *
     * @param email Destination address for the reset code.
     * @return [SendConfirmationData] with server retry metadata on success, or an error result.
     */
    suspend fun sendResetPasswordConfirmationToEmail(
        email: String
    ): AppResult<SendConfirmationData>

    /**
     * @param email Same email key as used for password-reset confirmation sends.
     * @return Remaining client-side cooldown in seconds before another send is allowed, or `0`.
     */
    fun getRemainingResetPasswordConfirmationDelayInSeconds(email: String): Int
}