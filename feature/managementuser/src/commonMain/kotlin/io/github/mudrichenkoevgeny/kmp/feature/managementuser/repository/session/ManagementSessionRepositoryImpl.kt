package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.session.ManagementSessionApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserSortValues
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSession

/**
 * Implements [ManagementSessionRepository] by forwarding administrative operations to [ManagementSessionApi].
 *
 * @param managementSessionApi Administrative HTTP endpoints for managing user sessions.
 */
class ManagementSessionRepositoryImpl(
    private val managementSessionApi: ManagementSessionApi
) : ManagementSessionRepository {

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
    ): AppResult<PagedResult<UserSession>> {
        return managementSessionApi.getSessions(
            pageNumber = pageNumber,
            pageSize = pageSize,
            sortBy = sortBy,
            sortOrder = sortOrder,
            userIds = userIds,
            userRoles = userRoles,
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
        ).mapSuccess { pagedPayload ->
            pagedPayload.mapItems { payload ->
                payload.toUserSession()
            }
        }
    }

    override suspend fun getSession(sessionId: String): AppResult<UserSession> {
        return managementSessionApi.getSession(sessionId).mapSuccess { payload ->
            payload.toUserSession()
        }
    }

    override suspend fun deleteSession(userId: UserId, sessionId: String): AppResult<Unit> {
        return managementSessionApi.deleteSession(
            userId = userId,
            sessionId = sessionId
        )
    }

    override suspend fun deleteAllUserSessions(userId: UserId): AppResult<Unit> {
        return managementSessionApi.deleteAllUserSessions(userId)
    }
}