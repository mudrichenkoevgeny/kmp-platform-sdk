package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

/**
 * Unified sign-in entry points for both client and management applications.
 *
 * Supports password-based authentication, multifactor verification (TOTP/Recovery codes),
 * and platform-specific identity providers.
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
     * Completes phone login using [phoneNumber] and [confirmationCode].
     *
     * @param phoneNumber E.164 or backend-normalized phone string.
     * @param confirmationCode One-time code delivered to the phone.
     * @return [AuthData] on success, or an error result.
     */
    suspend fun loginByPhone(phoneNumber: String, confirmationCode: String): AppResult<AuthData>

    /**
     * Signs in via an external [authProvider] using an identity [token].
     *
     * @param authProvider OAuth/OIDC/social backend integration.
     * @param token Provider-issued credential.
     * @return [AuthData] on success, or an error result.
     */
    suspend fun loginByExternalAuthProvider(authProvider: UserAuthProvider, token: String): AppResult<AuthData>

    /**
     * Completes the MFA flow using a time-based one-time password (TOTP).
     *
     * @param mfaToken Opaque intermediate token from the initial authentication step.
     * @param code Time-based verification code.
     * @return [AuthData] on success, or an error result.
     */
    suspend fun loginByTotp(mfaToken: String, code: String): AppResult<AuthData>

    /**
     * Completes the MFA flow using a static backup recovery code.
     *
     * @param mfaToken Opaque intermediate token from the initial authentication step.
     * @param code Single-use alphanumeric recovery code.
     * @return [AuthData] on success, or an error result.
     */
    suspend fun loginByTotpRecoveryCode(mfaToken: String, code: String): AppResult<AuthData>

    /**
     * Sends a login confirmation code to [phoneNumber].
     *
     * @param phoneNumber Target phone for the code.
     * @return [OtpConfirmation] with retry metadata on success, or an error result.
     */
    suspend fun sendLoginConfirmationToPhone(phoneNumber: String): AppResult<OtpConfirmation>

    /**
     * Returns the remaining client-side cooldown in seconds for [phoneNumber].
     *
     * @param phoneNumber Target phone key.
     * @return Remaining delay in seconds, or 0 if allowed immediately.
     */
    fun getRemainingLoginConfirmationDelayInSeconds(phoneNumber: String): Int
}