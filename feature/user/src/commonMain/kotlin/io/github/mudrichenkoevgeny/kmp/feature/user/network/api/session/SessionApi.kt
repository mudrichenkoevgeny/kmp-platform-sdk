package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.session.UserSessionResponse

interface SessionApi {
    suspend fun getSessions(): AppResult<List<UserSessionResponse>>
    suspend fun logout(): AppResult<Unit>
    suspend fun deleteSession(userSessionId: String): AppResult<Unit>
    suspend fun deleteAllOtherSessions(): AppResult<Unit>
}