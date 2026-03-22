package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.security.userIdentifiers

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.contract.CommonApiFields
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierExternalAuthProviderRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.useridentifiers.AddUserIdentifierPhoneRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.confirmation.SendConfirmationResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.useridentifier.UserIdentifierResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.security.useridentifiers.SecurityUserIdentifiersRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [SecurityUserIdentifiersApi] backed by [HttpClient]. */
class KtorSecurityUserIdentifiersApi(
    private val client: HttpClient
) : SecurityUserIdentifiersApi {

    override suspend fun getUserIdentifiers(): AppResult<List<UserIdentifierResponse>> = client.callResult {
        get(SecurityUserIdentifiersRoutes.GET_USER_IDENTIFIERS)
    }

    override suspend fun deleteUserIdentifier(id: String): AppResult<Unit> = client.callResult {
        delete(SecurityUserIdentifiersRoutes.DELETE_USER_IDENTIFIER.replace(
            "{${CommonApiFields.ID}}",
            id)
        )
    }

    override suspend fun addUserIdentifierEmail(
        request: AddUserIdentifierEmailRequest
    ): AppResult<UserIdentifierResponse> = client.callResult {
        post(SecurityUserIdentifiersRoutes.ADD_USER_IDENTIFIER_EMAIL) {
            setBody(request)
        }
    }

    override suspend fun addUserIdentifierPhone(
        request: AddUserIdentifierPhoneRequest
    ): AppResult<UserIdentifierResponse> = client.callResult {
        post(SecurityUserIdentifiersRoutes.ADD_USER_IDENTIFIER_PHONE) {
            setBody(request)
        }
    }

    override suspend fun addUserIdentifierExternalAuthProvider(
        request: AddUserIdentifierExternalAuthProviderRequest
    ): AppResult<UserIdentifierResponse> = client.callResult {
        post(SecurityUserIdentifiersRoutes.ADD_USER_IDENTIFIER_EXTERNAL_AUTH_PROVIDER) {
            setBody(request)
        }
    }

    override suspend fun sendAddEmailIdentifierConfirmation(
        request: SendConfirmationToEmailRequest
    ): AppResult<SendConfirmationResponse> = client.callResult {
        post(SecurityUserIdentifiersRoutes.SEND_ADD_EMAIL_IDENTIFIER_CONFIRMATION) {
            setBody(request)
        }
    }

    override suspend fun sendAddPhoneIdentifierConfirmation(
        request: SendConfirmationToPhoneRequest
    ): AppResult<SendConfirmationResponse> = client.callResult {
        post(SecurityUserIdentifiersRoutes.SEND_ADD_PHONE_IDENTIFIER_CONFIRMATION) {
            setBody(request)
        }
    }
}