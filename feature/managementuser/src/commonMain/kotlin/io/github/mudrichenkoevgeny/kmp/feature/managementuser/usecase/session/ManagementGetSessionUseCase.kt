package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session.ManagementSessionRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate

/**
 * Administratively retrieves specific session details.
 *
 * @param managementSessionRepository Administrative session management API.
 */
public class ManagementGetSessionUseCase(
    private val managementSessionRepository: ManagementSessionRepository
) {
    /**
     * @param sessionId Unique session identifier.
     * @return Detailed information of the target session model, or a mapped failure.
     */
    public suspend operator fun invoke(sessionId: String): AppResult<UserSessionPrivate> =
        managementSessionRepository.getSession(
            sessionId
        )
}
