package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.auth.markAsPublic
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByEmailConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByPhoneConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.auth.unlock.SelfManagementUnlockRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [SelfManagementUnlockApi] backed by [HttpClient]; login/unlock routes call [markAsPublic] so bearer auth is not applied. */
class KtorSelfManagementUnlockApi(
    private val client: HttpClient
) : SelfManagementUnlockApi {

    override suspend fun sendUnlockEmailConfirmation(
        request: SendConfirmationToEmailRequest
    ): AppResult<OtpConfirmationPayload> = client.callResult {
        post(SelfManagementUnlockRoutes.SEND_UNLOCK_EMAIL_CONFIRMATION) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun unlockByEmail(
        request: UnlockByEmailConfirmationRequest
    ): AppResult<Unit> = client.callResult {
        post(SelfManagementUnlockRoutes.UNLOCK_BY_EMAIL) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun sendUnlockPhoneConfirmation(
        request: SendConfirmationToPhoneRequest
    ): AppResult<OtpConfirmationPayload> = client.callResult {
        post(SelfManagementUnlockRoutes.SEND_UNLOCK_PHONE_CONFIRMATION) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun unlockByPhone(
        request: UnlockByPhoneConfirmationRequest
    ): AppResult<Unit> = client.callResult {
        post(SelfManagementUnlockRoutes.UNLOCK_BY_PHONE) {
            markAsPublic()
            setBody(request)
        }
    }

    override suspend fun unlockByExternalAuthProvider(
        request: UnlockByExternalAuthProviderRequest
    ): AppResult<Unit> = client.callResult {
        post(SelfManagementUnlockRoutes.UNLOCK_BY_EXTERNAL_PROVIDER) {
            markAsPublic()
            setBody(request)
        }
    }
}
