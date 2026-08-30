package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.auth.markAsPublic
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.data.AuthDataPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.login.LoginByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.auth.login.SelfManagementLoginRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [SelfManagementLoginApi] backed by [HttpClient]; login routes call [markAsPublic] so bearer auth is not applied. */
class KtorSelfManagementLoginApi(
    private val client: HttpClient
) : SelfManagementLoginApi {

    override suspend fun loginByEmail(
        request: LoginByEmailRequest
    ): AppResult<AuthDataPayload> = client.callResult {
        post(SelfManagementLoginRoutes.LOGIN_BY_EMAIL) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun loginByTotp(
        request: VerifyTotpPayload
    ): AppResult<AuthDataPayload> = client.callResult {
        post(SelfManagementLoginRoutes.LOGIN_BY_TOTP) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun loginByTotpRecoveryCode(
        request: VerifyTotpPayload
    ): AppResult<AuthDataPayload> = client.callResult {
        post(SelfManagementLoginRoutes.LOGIN_BY_TOTP_RECOVERY_CODE) {
            markAsPublic()
            setBody(request)
        }
    }
}