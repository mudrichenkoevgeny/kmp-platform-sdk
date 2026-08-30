package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.auth.markAsPublic
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.data.AuthDataPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.open.auth.login.OpenLoginRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [OpenLoginApi] backed by [HttpClient]; login routes call [markAsPublic] so bearer auth is not applied. */
class KtorOpenLoginApi(
    private val client: HttpClient
) : OpenLoginApi {

    override suspend fun loginByEmail(
        request: LoginByEmailRequest
    ): AppResult<AuthDataPayload> = client.callResult {
        post(OpenLoginRoutes.LOGIN_BY_EMAIL) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun loginByPhone(
        request: LoginByPhoneRequest
    ): AppResult<AuthDataPayload> = client.callResult {
        post(OpenLoginRoutes.LOGIN_BY_PHONE) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun loginByExternalAuthProvider(
        request: LoginByExternalAuthProviderRequest
    ): AppResult<AuthDataPayload> = client.callResult {
        post(OpenLoginRoutes.LOGIN_BY_EXTERNAL_AUTH_PROVIDER) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun loginByTotp(
        request: VerifyTotpPayload
    ): AppResult<AuthDataPayload> = client.callResult {
        post(OpenLoginRoutes.LOGIN_BY_TOTP) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun loginByTotpRecoveryCode(
        request: VerifyTotpPayload
    ): AppResult<AuthDataPayload> = client.callResult {
        post(OpenLoginRoutes.LOGIN_BY_TOTP_RECOVERY_CODE) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun sendLoginConfirmationToPhone(
        request: SendConfirmationToPhoneRequest
    ): AppResult<OtpConfirmationPayload> = client.callResult {
        post(OpenLoginRoutes.SEND_LOGIN_CONFIRMATION_TO_PHONE) {
            markAsPublic()
            setBody(request)
        }
    }
}