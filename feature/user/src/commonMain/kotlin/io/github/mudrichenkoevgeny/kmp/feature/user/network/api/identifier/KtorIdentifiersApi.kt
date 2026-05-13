package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.contract.CommonApiFields
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.EmailPasswordChangeRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.open.identifier.OpenIdentifierRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [IdentifiersApi] backed by [HttpClient]. */
class KtorIdentifiersApi(
    private val client: HttpClient
) : IdentifiersApi {

    override suspend fun getUserIdentifiers(): AppResult<PagedResult<UserIdentifierPayload>> =
        client.callResult {
            get(OpenIdentifierRoutes.GET_IDENTIFIERS)
        }

    override suspend fun deleteUserIdentifier(
        identifierId: UserIdentifierId
    ): AppResult<Unit> = client.callResult {
        delete(OpenIdentifierRoutes.DELETE_IDENTIFIER
            .replace(
                "{${CommonApiFields.ID}}",
                identifierId.asHexDashString()
            )
        )
    }

    override suspend fun addUserIdentifierEmail(
        request: AddUserIdentifierEmailRequest
    ): AppResult<UserIdentifierPayload> = client.callResult {
        post(OpenIdentifierRoutes.ADD_IDENTIFIER_EMAIL) {
            setBody(request)
        }
    }

    override suspend fun addUserIdentifierPhone(
        request: AddUserIdentifierPhoneRequest
    ): AppResult<UserIdentifierPayload> = client.callResult {
        post(OpenIdentifierRoutes.ADD_IDENTIFIER_PHONE) {
            setBody(request)
        }
    }

    override suspend fun addUserIdentifierExternalAuthProvider(
        request: AddUserIdentifierExternalAuthProviderRequest
    ): AppResult<UserIdentifierPayload> = client.callResult {
        post(OpenIdentifierRoutes.ADD_IDENTIFIER_EXTERNAL_AUTH_PROVIDER) {
            setBody(request)
        }
    }

    override suspend fun sendAddEmailIdentifierConfirmation(
        request: SendConfirmationToEmailRequest
    ): AppResult<OtpConfirmationPayload> = client.callResult {
        post(OpenIdentifierRoutes.SEND_ADD_EMAIL_IDENTIFIER_CONFIRMATION) {
            setBody(request)
        }
    }

    override suspend fun sendAddPhoneIdentifierConfirmation(
        request: SendConfirmationToPhoneRequest
    ): AppResult<OtpConfirmationPayload> = client.callResult {
        post(OpenIdentifierRoutes.SEND_ADD_PHONE_IDENTIFIER_CONFIRMATION) {
            setBody(request)
        }
    }

    override suspend fun emailChangePassword(request: EmailPasswordChangeRequest): AppResult<Unit> =
        client.callResult {
            post(OpenIdentifierRoutes.IDENTIFIER_EMAIL_CHANGE_PASSWORD) {
                setBody(request)
            }
        }
}