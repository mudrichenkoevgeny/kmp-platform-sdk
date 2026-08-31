package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session.ManagementSessionRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Administratively deletes all active sessions for the specified user.
 *
 * @param managementSessionRepository Administrative session management API.
 */
class ManagementDeleteAllUserSessionsUseCase(
    private val managementSessionRepository: ManagementSessionRepository
) {
    /**
     * @param userId Unique identifier of the target account.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend operator fun invoke(userId: UserId): AppResult<Unit> {
        return managementSessionRepository.deleteAllUserSessions(userId)
    }
}
