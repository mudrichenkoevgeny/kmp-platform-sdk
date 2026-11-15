package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session.ManagementSessionRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.userlist.ManagementUserSessionListParams

/**
 * Administrative retrieval of active user sessions for a specific target user.
 *
 * @param managementSessionRepository Administrative session management API.
 */
public class ManagementGetUserSessionsUseCase(
    private val managementSessionRepository: ManagementSessionRepository
) {
    /**
     * @param params Per-user session list query and filter parameters DTO.
     * @return Paginated result containing matching session models, or a mapped failure.
     */
    public suspend operator fun invoke(
        params: ManagementUserSessionListParams
    ): AppResult<PagedResult<UserSessionSummary>> = managementSessionRepository.getUserSessions(params)
}
