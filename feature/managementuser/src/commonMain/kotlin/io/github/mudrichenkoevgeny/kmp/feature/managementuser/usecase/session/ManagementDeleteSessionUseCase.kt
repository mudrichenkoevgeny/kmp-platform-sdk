package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session.ManagementSessionRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId

/**
 * Administratively deletes a specific session for the given user.
 *
 * @param managementSessionRepository Administrative session management API.
 */
public class ManagementDeleteSessionUseCase(
    private val managementSessionRepository: ManagementSessionRepository
) {
    /**
     * @param userId Unique identifier of the session owner.
     * @param sessionId Unique session identifier to revoke.
     * @return Empty success indicator, or a mapped failure.
     */
    public suspend operator fun invoke(
        userId: UserId,
        sessionId: UserSessionId
    ): AppResult<Unit> = managementSessionRepository.deleteSession(userId, sessionId)
}
