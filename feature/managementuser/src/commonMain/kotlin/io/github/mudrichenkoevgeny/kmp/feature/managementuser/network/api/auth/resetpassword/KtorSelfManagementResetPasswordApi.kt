package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.resetpassword.ResetPasswordApi
import io.github.mudrichenkoevgeny.kmp.feature.user.network.auth.markAsPublic
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.ResetPasswordRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.SendResetPasswordConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.auth.resetpassword.SelfManagementResetPasswordRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [ResetPasswordApi] backed by [HttpClient]; reset routes use [markAsPublic]. */
class KtorSelfManagementResetPasswordApi(
    private val client: HttpClient
) : ResetPasswordApi {

    override suspend fun resetPassword(
        request: ResetPasswordRequest
    ): AppResult<UserIdentifierPayload> = client.callResult {
        post(SelfManagementResetPasswordRoutes.RESET_PASSWORD) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun sendResetPasswordConfirmationToEmail(
        request: SendResetPasswordConfirmationRequest
    ): AppResult<OtpConfirmationPayload> = client.callResult {
        post(SelfManagementResetPasswordRoutes.SEND_RESET_PASSWORD_CONFIRMATION) {
            markAsPublic()
            setBody(request)
        }
    }
}