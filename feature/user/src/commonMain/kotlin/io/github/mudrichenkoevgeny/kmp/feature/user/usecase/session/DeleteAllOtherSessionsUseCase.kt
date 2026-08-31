package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository

/**
 * Deletes all sessions for the current authenticated account except the one used for this request.
 *
 * @param sessionRepository Remote session management API.
 */
class DeleteAllOtherSessionsUseCase(
    private val sessionRepository: SessionRepository
) {
    /**
     * @return Empty success indicator, or a mapped failure.
     */
    suspend operator fun invoke(): AppResult<Unit> {
        return sessionRepository.deleteAllOtherSessions()
    }
}
