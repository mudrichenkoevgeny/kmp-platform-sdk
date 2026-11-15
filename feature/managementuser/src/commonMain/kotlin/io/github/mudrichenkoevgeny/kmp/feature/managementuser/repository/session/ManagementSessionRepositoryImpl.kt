package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.session.ManagementSessionApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.mapper.pagedresult.mapItems
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.session.toUserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.globallist.ManagementGlobalSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.userlist.ManagementUserSessionListParams

/**
 * Implements [ManagementSessionRepository] by forwarding administrative operations to [ManagementSessionApi].
 *
 * @param managementSessionApi Administrative HTTP endpoints for managing user sessions.
 */
public class ManagementSessionRepositoryImpl(
    private val managementSessionApi: ManagementSessionApi
) : ManagementSessionRepository {

    override suspend fun getSessions(
        params: ManagementGlobalSessionListParams?
    ): AppResult<PagedResult<UserSessionSummary>> = managementSessionApi.getSessions(
        params
    ).mapSuccess { pagedPayload ->
        pagedPayload.mapItems { payload ->
            payload.toUserSessionSummary()
        }
    }

    override suspend fun getUserSessions(
        params: ManagementUserSessionListParams
    ): AppResult<PagedResult<UserSessionSummary>> = managementSessionApi.getUserSessions(
        params
    ).mapSuccess { pagedPayload ->
        pagedPayload.mapItems { userSessionPayload ->
            userSessionPayload.toUserSessionSummary()
        }
    }

    override suspend fun getSession(sessionId: String): AppResult<UserSessionPrivate> = managementSessionApi.getSession(
        sessionId
    ).mapSuccess { payload ->
        payload.toUserSessionPrivate()
    }

    override suspend fun deleteSession(
        userId: UserId,
        sessionId: String
    ): AppResult<Unit> = managementSessionApi.deleteSession(
        userId = userId,
        sessionId = sessionId
    )

    override suspend fun deleteAllUserSessions(userId: UserId): AppResult<Unit> =
        managementSessionApi.deleteAllUserSessions(
            userId
        )
}
