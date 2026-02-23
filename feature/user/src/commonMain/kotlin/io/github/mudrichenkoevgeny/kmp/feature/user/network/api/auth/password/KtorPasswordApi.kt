package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.password

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.auth.markAsPublic
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.ResetPasswordRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.SendResetPasswordConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.confirmation.SendConfirmationResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.useridentifier.UserIdentifierResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.auth.resetpassword.ResetPasswordRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class KtorPasswordApi(
    private val client: HttpClient
) : PasswordApi {

    override suspend fun resetPassword(
        request: ResetPasswordRequest
    ): AppResult<UserIdentifierResponse> = client.callResult {
        post(ResetPasswordRoutes.RESET_PASSWORD) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun sendResetPasswordConfirmationToEmail(
        request: SendResetPasswordConfirmationRequest
    ): AppResult<SendConfirmationResponse> = client.callResult {
        post(ResetPasswordRoutes.SEND_RESET_PASSWORD_CONFIRMATION) {
            markAsPublic()
            setBody(request)
        }
    }
}