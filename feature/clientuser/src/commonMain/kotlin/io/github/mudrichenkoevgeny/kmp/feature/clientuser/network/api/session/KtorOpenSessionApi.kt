package io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.session

import io.github.mudrichenkoevgeny.kmp.core.common.network.utils.callResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session.SessionApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.ListingParamNames
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiPaths
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.DeletedSessionsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.session.UserSessionPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.route.open.session.OpenSessionRoutes
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/** [SessionApi] backed by [HttpClient]. */
class KtorOpenSessionApi(
    private val client: HttpClient
) : SessionApi {

    override suspend fun getSessions(
        pageNumber: Int?,
        pageSize: Int?,
        sortBy: UserSortValues.UserSessionSortBy?,
        sortOrder: SortOrder?,
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
        get(OpenSessionRoutes.GET_SESSIONS) {
            parameter(ListingParamNames.Pagination.PAGE_NUMBER, pageNumber)
            parameter(ListingParamNames.Pagination.PAGE_SIZE, pageSize)
            parameter(ListingParamNames.Sort.SORT_BY, sortBy?.serialName)
            parameter(ListingParamNames.Sort.SORT_ORDER, sortOrder?.serialName)
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

    override suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSessionPayload> =
        client.callResult {
            get(
                OpenSessionRoutes.GET_SESSION.replace(
                    "{${UserApiPaths.SESSION_ID}}",
                    userSessionId.asHexDashString()
                )
            )
        }

    override suspend fun logout(): AppResult<Unit> = client.callResult {
        post(OpenSessionRoutes.LOGOUT)
    }

    override suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit> =
        client.callResult {
            delete(
                OpenSessionRoutes.DELETE_SESSION.replace(
                    "{${UserApiPaths.SESSION_ID}}",
                    userSessionId.asHexDashString()
                )
            )
        }

    override suspend fun deleteAllOtherSessions(): AppResult<DeletedSessionsPayload> =
        client.callResult {
            delete(OpenSessionRoutes.DELETE_ALL_OTHER_SESSIONS)
        }

    override suspend fun reauthenticateSession(request: VerifyTotpPayload): AppResult<Unit> =
        client.callResult {
            post(OpenSessionRoutes.REAUTHENTICATE_SESSION) {
                setBody(request)
            }
        }
}