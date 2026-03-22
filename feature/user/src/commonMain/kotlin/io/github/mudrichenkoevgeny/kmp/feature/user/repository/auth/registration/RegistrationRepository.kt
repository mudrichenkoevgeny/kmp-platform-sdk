package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData

/**
 * Email-based registration: confirm address, then complete sign-up and receive auth material.
 */
interface RegistrationRepository {
    /**
     * Registers a new account with [email], [password], and [confirmationCode] from the email step.
     *
     * @param email New account email.
     * @param password Chosen password.
     * @param confirmationCode One-time code sent to [email].
     * @return [AuthData] on success, or an error result.
     */
    suspend fun registerByEmail(email: String, password: String, confirmationCode: String): AppResult<AuthData>

    /**
     * Sends a registration confirmation code to [email], respecting client-side rate limits.
     *
     * @param email Destination for the registration code.
     * @return [SendConfirmationData] with retry metadata on success, or an error result (including
     * when throttled before the network call).
     */
    suspend fun sendRegistrationConfirmationToEmail(email: String): AppResult<SendConfirmationData>

    /**
     * @param email Same email key as used for registration confirmation sends.
     * @return Remaining client-side cooldown in seconds before another send is allowed, or `0`.
     */
    fun getRemainingRegistrationConfirmationDelayInSeconds(email: String): Int
}