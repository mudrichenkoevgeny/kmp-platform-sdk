package io.github.mudrichenkoevgeny.kmp.feature.user.repository.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId

interface SessionRepository {

    suspend fun getSessions(): AppResult<PagedResult<UserSession>>

    suspend fun getSession(userSessionId: UserSessionId): AppResult<UserSession>

    suspend fun logout(): AppResult<Unit>

    suspend fun deleteSession(userSessionId: UserSessionId): AppResult<Unit>

    suspend fun deleteAllOtherSessions(): AppResult<Unit>

    suspend fun reauthenticateSession(mfaToken: String, code: String): AppResult<Unit>
}