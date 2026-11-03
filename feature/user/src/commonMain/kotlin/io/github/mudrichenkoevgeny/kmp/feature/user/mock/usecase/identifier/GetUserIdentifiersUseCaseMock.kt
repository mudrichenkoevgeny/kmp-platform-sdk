package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.list.SelfIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierSummary

@InternalApi
class GetUserIdentifiersUseCaseMock(
    identifierRepository: IdentifierRepository = IdentifierRepositoryMock()
) : GetUserIdentifiersUseCase(identifierRepository) {

    var executeCalls = 0
    var resultProvider: (params: SelfIdentifierListParams?) -> AppResult<PagedResult<UserIdentifierSummary>> = { _ ->
        AppResult.Success(createTestPagedResult(listOf(createTestUserIdentifierSummary())))
    }

    override suspend fun invoke(
        params: SelfIdentifierListParams?
    ): AppResult<PagedResult<UserIdentifierSummary>> {
        executeCalls++
        return resultProvider(params)
    }
}
