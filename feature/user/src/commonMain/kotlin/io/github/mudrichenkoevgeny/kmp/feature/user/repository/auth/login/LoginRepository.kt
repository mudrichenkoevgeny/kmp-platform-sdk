package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.SendConfirmationData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider

/**
 * Sign-in entry points: email/password, phone with SMS-style confirmation, and external identity
 * providers, plus throttled send-code for phone login.
 */
interface LoginRepository {
    /**
     * Authenticates with [email] and [password].
     *
     * @param email Account email.
     * @param password Account password.
     * @return [AuthData] on success, or an error result.
     */
    suspend fun loginByEmail(email: String, password: String): AppResult<AuthData>

    /**
     * Completes phone login using [phoneNumber] and [confirmationCode] from the SMS or voice step.
     *
     * @param phoneNumber E.164 or backend-normalized phone string.
     * @param confirmationCode One-time code delivered to the phone.
     * @return [AuthData] on success, or an error result.
     */
    suspend fun loginByPhone(phoneNumber: String, confirmationCode: String): AppResult<AuthData>

    /**
     * Signs in via an external [authProvider] using an identity [token] from that provider SDK.
     *
     * @param authProvider Which OAuth/OIDC/social backend integration to use.
     * @param token Provider-issued credential passed to the backend.
     * @return [AuthData] on success, or an error result.
     */
    suspend fun loginByExternalAuthProvider(authProvider: UserAuthProvider, token: String): AppResult<AuthData>

    /**
     * Sends a login confirmation code to [phoneNumber], respecting client-side rate limits.
     *
     * @param phoneNumber Target phone for the login code.
     * @return [SendConfirmationData] with retry metadata on success, or an error result (including
     * when throttled before the network call).
     */
    suspend fun sendLoginConfirmationToPhone(phoneNumber: String): AppResult<SendConfirmationData>

    /**
     * @param phoneNumber Same phone key as used for login confirmation sends.
     * @return Remaining client-side cooldown in seconds before another send is allowed, or `0`.
     */
    fun getRemainingLoginConfirmationDelayInSeconds(phoneNumber: String): Int
}