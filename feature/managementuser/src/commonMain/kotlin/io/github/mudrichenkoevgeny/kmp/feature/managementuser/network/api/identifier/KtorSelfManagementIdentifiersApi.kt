package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.ListingParamNames
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiPaths
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.security.password.EmailPasswordChangeRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.identifier.SelfManagementIdentifierRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [SelfManagementIdentifiersApi] backed by [HttpClient]. */
class KtorSelfManagementIdentifiersApi(
    private val client: HttpClient
) : SelfManagementIdentifiersApi {

    override suspend fun getUserIdentifier(
        userIdentifierId: UserIdentifierId
    ): AppResult<UserIdentifierPayload> = client.callResult {
        get(
            SelfManagementIdentifierRoutes.GET_IDENTIFIER
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
        get(SelfManagementIdentifierRoutes.GET_IDENTIFIERS) {
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

    override suspend fun emailChangePassword(request: EmailPasswordChangeRequest): AppResult<Unit> =
        client.callResult {
            post(SelfManagementIdentifierRoutes.IDENTIFIER_EMAIL_CHANGE_PASSWORD) {
                setBody(request)
            }
        }
}