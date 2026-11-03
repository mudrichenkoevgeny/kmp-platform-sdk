package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session.ManagementSessionRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.globallist.ManagementGlobalSessionListParams

/**
 * Administrative retrieval of user sessions based on filters.
 *
 * @param managementSessionRepository Administrative session management API.
 */
class ManagementGetSessionsUseCase(
    private val managementSessionRepository: ManagementSessionRepository
) {
    /**
     * @param params Administrative session list query and filter parameters DTO.
     * @return Paginated result containing matching user session models, or a mapped failure.
     */
    suspend operator fun invoke(
        params: ManagementGlobalSessionListParams? = null
    ): AppResult<PagedResult<UserSessionSummary>> {
        return managementSessionRepository.getSessions(params)
    }
}
