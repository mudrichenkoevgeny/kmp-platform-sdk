package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId

/**
 * Deletes a specific active session for the current authenticated account.
 *
 * @param sessionRepository Remote session management API.
 */
class DeleteSessionUseCase(
    private val sessionRepository: SessionRepository
) {
    /**
     * @param userSessionId Unique session identifier to revoke.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend operator fun invoke(userSessionId: UserSessionId): AppResult<Unit> {
        return sessionRepository.deleteSession(userSessionId)
    }
}
