package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.ListingParamNames
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiPaths
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.identifier.ManagementIdentifierRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/** [ManagementIdentifierApi] backed by [HttpClient]. */
class KtorManagementIdentifierApi(
    private val client: HttpClient
) : ManagementIdentifierApi {

    override suspend fun getIdentifiers(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserIdentifierSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        identifiers: List<String>?
    ): AppResult<PagedResult<UserIdentifierPayload>> = client.callResult {
        get(ManagementIdentifierRoutes.GET_IDENTIFIERS) {
            parameter(ListingParamNames.Pagination.PAGE_NUMBER, pageNumber)
            parameter(ListingParamNames.Pagination.PAGE_SIZE, pageSize)
            parameter(ListingParamNames.Sort.SORT_BY, sortBy?.serialName)
            parameter(ListingParamNames.Sort.SORT_ORDER, sortOrder?.serialName)
            userIds?.forEach { userId ->
                parameter(UserFilterValues.UserIdentifierFilterValues.USER_ID, userId)
            }
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

    override suspend fun getIdentifier(
        identifierId: String
    ): AppResult<UserIdentifierPayload> = client.callResult {
        get(ManagementIdentifierRoutes.GET_IDENTIFIER) {
            parameter(UserApiPaths.USER_IDENTIFIER_ID, identifierId)
        }
    }

    override suspend fun deleteIdentifier(
        userId: UserId,
        identifierId: String
    ): AppResult<Unit> = client.callResult {
        delete(ManagementIdentifierRoutes.DELETE_IDENTIFIER) {
            parameter(UserApiPaths.USER_ID, userId.value)
            parameter(UserApiPaths.USER_IDENTIFIER_ID, identifierId)
        }
    }
}