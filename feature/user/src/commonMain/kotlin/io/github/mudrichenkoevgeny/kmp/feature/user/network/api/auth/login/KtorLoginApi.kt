package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.auth.markAsPublic
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.AuthDataResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.confirmation.SendConfirmationResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.auth.login.LoginRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class KtorLoginApi(
    private val client: HttpClient
) : LoginApi {

    override suspend fun loginByEmail(
        request: LoginByEmailRequest
    ): AppResult<AuthDataResponse> = client.callResult {
        post(LoginRoutes.LOGIN_BY_EMAIL) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun loginByPhone(
        request: LoginByPhoneRequest
    ): AppResult<AuthDataResponse> = client.callResult {
        post(LoginRoutes.LOGIN_BY_PHONE) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun loginByExternalAuthProvider(
        request: LoginByExternalAuthProviderRequest
    ): AppResult<AuthDataResponse> = client.callResult {
        post(LoginRoutes.LOGIN_BY_EXTERNAL_AUTH_PROVIDER) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun sendLoginConfirmationToPhone(
        request: SendConfirmationToPhoneRequest
    ): AppResult<SendConfirmationResponse> = client.callResult {
        post(LoginRoutes.SEND_LOGIN_CONFIRMATION_TO_PHONE) {
            markAsPublic()
            setBody(request)
        }
    }
}