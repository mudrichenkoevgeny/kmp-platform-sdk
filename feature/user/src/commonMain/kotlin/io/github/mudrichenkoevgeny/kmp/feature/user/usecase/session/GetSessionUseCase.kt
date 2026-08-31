package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId

/**
 * Returns details of a specific session owned by the current authenticated account.
 *
 * @param sessionRepository Remote session management API.
 */
class GetSessionUseCase(
    private val sessionRepository: SessionRepository
) {
    /**
     * @param userSessionId Unique session identifier.
     * @return Detailed session model or a mapped failure.
     */
    suspend operator fun invoke(userSessionId: UserSessionId): AppResult<UserSession> {
        return sessionRepository.getSession(userSessionId)
    }
}
