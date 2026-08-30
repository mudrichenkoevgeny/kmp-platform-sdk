package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.data.AuthDataPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest

/** Remote login and phone confirmation entry points for the user feature. */
interface OpenLoginApi {
    /**
     * Signs in with email credentials.
     *
     * @param request Email and secret payload from the shared contract.
     * @return Session tokens and related auth payload, or a mapped failure.
     */
    suspend fun loginByEmail(request: LoginByEmailRequest): AppResult<AuthDataPayload>

    /**
     * Signs in with phone credentials (e.g. after OTP verification).
     *
     * @param request Phone login payload from the shared contract.
     * @return Session tokens and related auth payload, or a mapped failure.
     */
    suspend fun loginByPhone(request: LoginByPhoneRequest): AppResult<AuthDataPayload>

    /**
     * Signs in via an external identity provider (OAuth / social).
     *
     * @param request Provider-specific login payload from the shared contract.
     * @return Session tokens and related auth payload, or a mapped failure.
     */
    suspend fun loginByExternalAuthProvider(
        request: LoginByExternalAuthProviderRequest
    ): AppResult<AuthDataPayload>

    /**
     * Completes multifactor authentication using a Time-based One-Time Password (TOTP).
     *
     * @param request Verification payload containing the current TOTP token.
     * @return Session tokens and updated auth payload upon successful verification, or a mapped failure.
     */
    suspend fun loginByTotp(
        request: VerifyTotpPayload
    ): AppResult<AuthDataPayload>

    /**
     * Completes multifactor authentication and account access recovery using a static backup code.
     *
     * @param request Verification payload containing the unused recovery code.
     * @return Session tokens and updated auth payload upon successful verification, or a mapped failure.
     */
    suspend fun loginByTotpRecoveryCode(
        request: VerifyTotpPayload
    ): AppResult<AuthDataPayload>

    /**
     * Sends a login confirmation challenge to the user phone (e.g. SMS / OTP).
     *
     * @param request Target phone and channel details from the shared contract.
     * @return Confirmation dispatch result, or a mapped failure.
     */
    suspend fun sendLoginConfirmationToPhone(
        request: SendConfirmationToPhoneRequest
    ): AppResult<OtpConfirmationPayload>
}