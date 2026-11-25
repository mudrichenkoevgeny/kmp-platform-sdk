package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.datasource

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteAllOtherSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.toUserSessionIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.list.SelfSessionListParams

/**
 * [SessionListDataSource] implementation for self-service / client active sessions list.
 *
 * @param getSessionsUseCase Use case fetching current active sessions.
 * @param deleteSessionUseCase Use case revoking a specific session.
 * @param deleteAllOtherSessionsUseCase Use case revoking all sessions except current.
 */
public class SelfSessionListDataSource(
    private val getSessionsUseCase: GetSessionsUseCase,
    private val deleteSessionUseCase: DeleteSessionUseCase,
    private val deleteAllOtherSessionsUseCase: DeleteAllOtherSessionsUseCase
) : SessionListDataSource {

    public override val isRevokeAvailable: Boolean = true
    public override val isRevokeAllOthersAvailable: Boolean = true

    public override suspend fun getSessions(query: SessionListQuery): AppResult<PagedResult<UserSessionSummary>> {
        val params = SelfSessionListParams(
            pageNumber = query.pageNumber
        )
        return getSessionsUseCase(params)
    }

    public override suspend fun revokeSession(
        sessionId: String,
        userId: String?
    ): AppResult<Unit> {
        val typedSessionId = sessionId.toUserSessionIdOrThrow()
        return deleteSessionUseCase(typedSessionId)
    }

    public override suspend fun revokeAllOtherSessions(userId: String?): AppResult<Unit> =
        deleteAllOtherSessionsUseCase()
}
