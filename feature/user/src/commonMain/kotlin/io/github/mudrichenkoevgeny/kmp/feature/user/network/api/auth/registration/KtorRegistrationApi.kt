package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.auth.markAsPublic
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.register.RegisterByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.AuthDataResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.confirmation.SendConfirmationResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.auth.register.RegisterRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [RegistrationApi] backed by [HttpClient]; public routes use [markAsPublic]. */
class KtorRegistrationApi(
    private val client: HttpClient
) : RegistrationApi {

    override suspend fun registerByEmail(
        request: RegisterByEmailRequest
    ): AppResult<AuthDataResponse> = client.callResult {
        post(RegisterRoutes.REGISTER_BY_EMAIL) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun sendRegistrationConfirmationToEmail(
        request: SendConfirmationToEmailRequest
    ): AppResult<SendConfirmationResponse> = client.callResult {
        post(RegisterRoutes.SEND_REGISTER_CONFIRMATION_TO_EMAIL) {
            markAsPublic()
            setBody(request)
        }
    }
}