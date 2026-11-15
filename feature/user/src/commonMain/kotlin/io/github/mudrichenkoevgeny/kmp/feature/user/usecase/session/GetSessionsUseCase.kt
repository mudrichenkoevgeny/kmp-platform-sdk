package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.list.SelfSessionListParams

/**
 * Retrieves a paginated list of active sessions for the current account.
 *
 * @param sessionRepository Session management repository.
 */
public open class GetSessionsUseCase(
    private val sessionRepository: SessionRepository
) {
    /**
     * @param params Query and filter parameters DTO.
     * @return Paginated result containing [UserSessionSummary] items on success, or a mapped failure.
     */
    public open suspend operator fun invoke(
        params: SelfSessionListParams? = null
    ): AppResult<PagedResult<UserSessionSummary>> = sessionRepository.getSessions(params)
}
