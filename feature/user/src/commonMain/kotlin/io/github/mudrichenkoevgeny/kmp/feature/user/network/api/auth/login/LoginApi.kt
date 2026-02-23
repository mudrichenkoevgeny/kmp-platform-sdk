package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.AuthDataResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.confirmation.SendConfirmationResponse

interface LoginApi {
    suspend fun loginByEmail(request: LoginByEmailRequest): AppResult<AuthDataResponse>
    suspend fun loginByPhone(request: LoginByPhoneRequest): AppResult<AuthDataResponse>
    suspend fun loginByExternalAuthProvider(
        request: LoginByExternalAuthProviderRequest
    ): AppResult<AuthDataResponse>
    suspend fun sendLoginConfirmationToPhone(
        request: SendConfirmationToPhoneRequest
    ): AppResult<SendConfirmationResponse>
}