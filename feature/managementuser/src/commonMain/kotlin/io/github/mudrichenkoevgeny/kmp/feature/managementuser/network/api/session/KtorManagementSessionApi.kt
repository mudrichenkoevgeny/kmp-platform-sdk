package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.session

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.ListingParamNames
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiPaths
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.management.session.ManagementSessionRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/** [ManagementSessionApi] backed by [HttpClient]. */
class KtorManagementSessionApi(
    private val client: HttpClient
) : ManagementSessionApi {

    override suspend fun getSessions(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserSessionSortBy?,
        sortOrder: SortOrder?,
        userIds: List<String>?,
        userRoles: List<UserRole>?,
        identifiers: List<String>?,
        identifierIds: List<String>?,
        userAuthProviders: List<UserAuthProvider>?,
        clientTypes: List<ClientType>?,
        userAgents: List<String>?,
        ipAddresses: List<String>?,
        languages: List<String>?,
        deviceIds: List<String>?,
        deviceNames: List<String>?,
        appVersions: List<String>?,
        operationSystemVersions: List<String>?
    ): AppResult<PagedResult<UserSessionPayload>> = client.callResult {
        get(ManagementSessionRoutes.GET_SESSIONS) {
            parameter(ListingParamNames.Pagination.PAGE_NUMBER, pageNumber)
            parameter(ListingParamNames.Pagination.PAGE_SIZE, pageSize)
            parameter(ListingParamNames.Sort.SORT_BY, sortBy?.serialName)
            parameter(ListingParamNames.Sort.SORT_ORDER, sortOrder?.serialName)
            userIds?.forEach { userId ->
                parameter(UserFilterValues.UserSessionFilterValues.USER_ID, userId)
            }
            userRoles?.forEach { role ->
                parameter(UserFilterValues.UserSessionFilterValues.USER_ROLE, role.serialName)
            }
            identifiers?.forEach { identifier ->
                parameter(UserFilterValues.UserSessionFilterValues.IDENTIFIER, identifier)
            }
            identifierIds?.forEach { identifierId ->
                parameter(UserFilterValues.UserSessionFilterValues.IDENTIFIER_ID, identifierId)
            }
            userAuthProviders?.forEach { provider ->
                parameter(UserFilterValues.UserSessionFilterValues.USER_AUTH_PROVIDER, provider.serialName)
            }
            clientTypes?.forEach { type ->
                parameter(UserFilterValues.UserSessionFilterValues.CLIENT_TYPE, type.serialName)
            }
            userAgents?.forEach { agent ->
                parameter(UserFilterValues.UserSessionFilterValues.USER_AGENT, agent)
            }
            ipAddresses?.forEach { ip ->
                parameter(UserFilterValues.UserSessionFilterValues.IP_ADDRESS, ip)
            }
            languages?.forEach { lang ->
                parameter(UserFilterValues.UserSessionFilterValues.LANGUAGE, lang)
            }
            deviceIds?.forEach { id ->
                parameter(UserFilterValues.UserSessionFilterValues.DEVICE_ID, id)
            }
            deviceNames?.forEach { name ->
                parameter(UserFilterValues.UserSessionFilterValues.DEVICE_NAME, name)
            }
            appVersions?.forEach { version ->
                parameter(UserFilterValues.UserSessionFilterValues.APP_VERSION, version)
            }
            operationSystemVersions?.forEach { osVersion ->
                parameter(UserFilterValues.UserSessionFilterValues.OPERATION_SYSTEM_VERSION, osVersion)
            }
        }
    }

    override suspend fun getSession(sessionId: String): AppResult<UserSessionPayload> = client.callResult {
        get(ManagementSessionRoutes.GET_SESSION) {
            parameter(UserApiPaths.SESSION_ID, sessionId)
        }
    }

    override suspend fun deleteSession(userId: UserId, sessionId: String): AppResult<Unit> = client.callResult {
        delete(ManagementSessionRoutes.DELETE_SESSION) {
            parameter(UserApiPaths.USER_ID, userId.value)
            parameter(UserApiPaths.SESSION_ID, sessionId)
        }
    }

    override suspend fun deleteAllUserSessions(userId: UserId): AppResult<Unit> = client.callResult {
        delete(ManagementSessionRoutes.DELETE_ALL_USER_SESSIONS) {
            parameter(UserApiPaths.USER_ID, userId.value)
        }
    }
}