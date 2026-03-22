package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.AuthDataResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.confirmation.SendConfirmationResponse

/** Remote login and phone confirmation entry points for the user feature. */
interface LoginApi {
    /**
     * Signs in with email credentials.
     *
     * @param request Email and secret payload from the shared contract.
     * @return Session tokens and related auth payload, or a mapped failure.
     */
    suspend fun loginByEmail(request: LoginByEmailRequest): AppResult<AuthDataResponse>

    /**
     * Signs in with phone credentials (e.g. after OTP verification).
     *
     * @param request Phone login payload from the shared contract.
     * @return Session tokens and related auth payload, or a mapped failure.
     */
    suspend fun loginByPhone(request: LoginByPhoneRequest): AppResult<AuthDataResponse>

    /**
     * Signs in via an external identity provider (OAuth / social).
     *
     * @param request Provider-specific login payload from the shared contract.
     * @return Session tokens and related auth payload, or a mapped failure.
     */
    suspend fun loginByExternalAuthProvider(
        request: LoginByExternalAuthProviderRequest
    ): AppResult<AuthDataResponse>

    /**
     * Sends a login confirmation challenge to the user phone (e.g. SMS / OTP).
     *
     * @param request Target phone and channel details from the shared contract.
     * @return Confirmation dispatch result, or a mapped failure.
     */
    suspend fun sendLoginConfirmationToPhone(
        request: SendConfirmationToPhoneRequest
    ): AppResult<SendConfirmationResponse>
}