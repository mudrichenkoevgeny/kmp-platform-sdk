package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.ListingParamNames
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiPaths
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
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [OpenIdentifiersApi] backed by [HttpClient]. */
class KtorOpenIdentifiersApi(
    private val client: HttpClient
) : OpenIdentifiersApi {

    override suspend fun getUserIdentifier(
        userIdentifierId: UserIdentifierId
    ): AppResult<UserIdentifierPayload> = client.callResult {
        get(
            OpenIdentifierRoutes.GET_IDENTIFIER
                .replace(
                    "{${UserApiPaths.USER_IDENTIFIER_ID}}",
                    userIdentifierId.asHexDashString()
                )
        )
    }

    override suspend fun getUserIdentifiers(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): AppResult<PagedResult<UserIdentifierPayload>> = client.callResult {
        get(OpenIdentifierRoutes.GET_IDENTIFIERS) {
            parameter(ListingParamNames.Pagination.PAGE_NUMBER, pageNumber)
            parameter(ListingParamNames.Pagination.PAGE_SIZE, pageSize)
            parameter(ListingParamNames.Sort.SORT_BY, sortBy?.serialName)
            parameter(ListingParamNames.Sort.SORT_ORDER, sortOrder?.serialName)
            userAuthProviders?.forEach { userAuthProvider ->
                parameter(
                    UserFilterValues.UserIdentifierFilterValues.USER_AUTH_PROVIDER,
                    userAuthProvider.serialName
                )
            }
            identifiers?.forEach { identifier ->
                parameter(UserFilterValues.UserIdentifierFilterValues.IDENTIFIER, identifier)
            }
        }
    }

    override suspend fun deleteUserIdentifier(
        identifierId: UserIdentifierId
    ): AppResult<Unit> = client.callResult {
        delete(
            OpenIdentifierRoutes.DELETE_IDENTIFIER
                .replace(
                    "{${UserApiPaths.USER_IDENTIFIER_ID}}",
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