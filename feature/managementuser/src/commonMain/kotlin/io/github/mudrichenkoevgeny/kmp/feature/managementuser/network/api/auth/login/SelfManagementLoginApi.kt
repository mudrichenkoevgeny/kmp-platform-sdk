package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.data.AuthDataPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest

/** Remote login and second-factor verification entry points for the user feature. */
interface SelfManagementLoginApi {
    /**
     * Signs in with email credentials.
     *
     * @param request Email and secret payload from the shared contract.
     * @return Session tokens and related auth payload, or a mapped failure.
     */
    suspend fun loginByEmail(request: LoginByEmailRequest): AppResult<AuthDataPayload>

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
}