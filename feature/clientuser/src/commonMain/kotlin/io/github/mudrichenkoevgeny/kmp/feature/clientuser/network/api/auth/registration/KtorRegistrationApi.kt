package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.network.httprequest.markAsPublic
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.data.AuthDataPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.registration.RegistrationByEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.open.auth.registration.OpenRegistrationRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [RegistrationApi] backed by [HttpClient]; public routes use [markAsPublic]. */
class KtorRegistrationApi(
    private val client: HttpClient
) : RegistrationApi {

    override suspend fun registerByEmail(
        request: RegistrationByEmailRequest
    ): AppResult<AuthDataPayload> = client.callResult {
        post(OpenRegistrationRoutes.REGISTRATION_BY_EMAIL) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun sendRegistrationConfirmationToEmail(
        request: SendConfirmationToEmailRequest
    ): AppResult<OtpConfirmationPayload> = client.callResult {
        post(OpenRegistrationRoutes.SEND_REGISTRATION_CONFIRMATION_TO_EMAIL) {
            markAsPublic()
            setBody(request)
        }
    }
}