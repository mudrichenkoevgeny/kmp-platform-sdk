package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate

/**
 * Retrieves details for a specific active session.
 *
 * @param sessionRepository Session management repository.
 */
public open class GetSessionUseCase(
    private val sessionRepository: SessionRepository
) {
    /**
     * @param userSessionId Session ID.
     * @return [UserSessionPrivate] on success, or a mapped failure.
     */
    public open suspend operator fun invoke(userSessionId: UserSessionId): AppResult<UserSessionPrivate> =
        sessionRepository.getSession(
            userSessionId
        )
}
