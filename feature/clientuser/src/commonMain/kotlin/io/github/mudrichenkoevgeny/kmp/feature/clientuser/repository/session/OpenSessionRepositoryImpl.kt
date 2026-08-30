package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session.SessionApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.verifytotp.VerifyTotpPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSession

/**
 * Implements [SessionRepository] using [SessionApi].
 *
 * @param sessionApi HTTP endpoints for session management and re-authentication.
 */
class OpenSessionRepositoryImpl(
    private val sessionApi: SessionApi,
    private val userStorage: UserStorage
) : SessionRepository {

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
    ): AppResult<PagedResult<UserSession>> {
        val networkResult = sessionApi.getSessions(
            pageNumber = pageNumber,
            pageSize = pageSize,
            sortBy = sortBy,
            sortOrder = sortOrder,
            identifiers = identifiers,
            identifierIds = identifierIds,
            userAuthProviders = userAuthProviders,
            clientTypes = clientTypes,
            userAgents = userAgents,
            ipAddresses = ipAddresses,
            languages = languages,
            deviceIds = deviceIds,
            deviceNames = deviceNames,
            appVersions = appVersions,
            operationSystemVersions = operationSystemVersions
        )
        return when (networkResult) {
            is AppResult.Success -> {
                userStorage.updateUserSessionsPayloadList(networkResult.data)
                networkResult.mapSuccess { pagedPayload ->
                    pagedPayload.mapItems { it.toUserSession() }
                }
            }
            is AppResult.Error -> {
                val cachedResult = userStorage.getUserSessionsList(
                    pageNumber = pageNumber,
                    pageSize = pageSize,
                    sortBy = sortBy,
                    sortOrder = sortOrder,
                    userIds = null,
                    userRoles = null,
                    identifiers = identifiers,
                    identifierIds = identifierIds,
                    userAuthProviders = userAuthProviders,
                    clientTypes = clientTypes,
                    userAgents = userAgents,
                    ipAddresses = ipAddresses,
                    languages = languages,
                    deviceIds = deviceIds,
                    deviceNames = deviceNames,
                    appVersions = appVersions,
                    operationSystemVersions = operationSystemVersions
                )
                if (cachedResult.items.isNotEmpty()) {
                    AppResult.Success(cachedResult)
                } else {
                    networkResult
                }
            }
        }
    }

    override suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSession> {
        val networkResult = sessionApi.getSession(userSessionId)
        return when (networkResult) {
            is AppResult.Success -> {
                val session = networkResult.data.toUserSession()
                userStorage.addUserSession(session)
                AppResult.Success(session)
            }
            is AppResult.Error -> {
                val cachedList = userStorage.getUserSessionsList(
                    pageNumber = null,
                    pageSize = null,
                    sortBy = null,
                    sortOrder = null,
                    userIds = null,
                    userRoles = null,
                    identifiers = null,
                    identifierIds = null,
                    userAuthProviders = null,
                    clientTypes = null,
                    userAgents = null,
                    ipAddresses = null,
                    languages = null,
                    deviceIds = null,
                    deviceNames = null,
                    appVersions = null,
                    operationSystemVersions = null
                )
                val cachedSession = cachedList.items.find { userSession ->
                    userSession.id == userSessionId
                }
                if (cachedSession != null) {
                    AppResult.Success(cachedSession)
                } else {
                    networkResult
                }
            }
        }
    }

    override suspend fun logout(): AppResult<Unit> {
        val networkResult = sessionApi.logout()
        if (networkResult is AppResult.Success) {
            userStorage.clear()
        }
        return networkResult
    }

    override suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit> {
        val networkResult = sessionApi.deleteSession(userSessionId)
        if (networkResult is AppResult.Success) {
            userStorage.removeUserSession(userSessionId)
        }
        return networkResult
    }

    override suspend fun deleteAllOtherSessions(): AppResult<Unit> {
        val networkResult = sessionApi.deleteAllOtherSessions()
        if (networkResult is AppResult.Success) {
            val deletedSessionIdsSet = networkResult.data.deletedSessionIds.toSet()
            if (deletedSessionIdsSet.isNotEmpty()) {
                val cachedList = userStorage.getUserSessionsList(
                    pageNumber = null,
                    pageSize = null,
                    sortBy = null,
                    sortOrder = null,
                    userIds = null,
                    userRoles = null,
                    identifiers = null,
                    identifierIds = null,
                    userAuthProviders = null,
                    clientTypes = null,
                    userAgents = null,
                    ipAddresses = null,
                    languages = null,
                    deviceIds = null,
                    deviceNames = null,
                    appVersions = null,
                    operationSystemVersions = null
                )
                val idsToRemove = cachedList.items
                    .map { it.id }
                    .filter { it.value.toHexDashString() in deletedSessionIdsSet }

                if (idsToRemove.isNotEmpty()) {
                    userStorage.removeUserSessions(idsToRemove)
                }
            }
        }
        return networkResult.mapSuccess { }
    }

    override suspend fun reauthenticateSession(mfaToken: String, code: String): AppResult<Unit> {
        return sessionApi.reauthenticateSession(
            VerifyTotpPayload(
                mfaToken = mfaToken,
                code = code
            )
        )
    }
}