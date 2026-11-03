package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.session.SessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.list.SelfSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionSummary

@InternalApi
class GetSessionsUseCaseMock(
    sessionRepository: SessionRepository = SessionRepositoryMock()
) : GetSessionsUseCase(sessionRepository) {

    var executeCalls = 0
    var resultProvider: (params: SelfSessionListParams?) -> AppResult<PagedResult<UserSessionSummary>> = { _ ->
        AppResult.Success(createTestPagedResult(listOf(createTestUserSessionSummary())))
    }

    override suspend fun invoke(
        params: SelfSessionListParams?
    ): AppResult<PagedResult<UserSessionSummary>> {
        executeCalls++
        return resultProvider(params)
    }
}
