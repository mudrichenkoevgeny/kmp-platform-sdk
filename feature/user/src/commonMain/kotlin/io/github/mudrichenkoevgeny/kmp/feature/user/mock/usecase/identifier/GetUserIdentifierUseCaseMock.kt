package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifierUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierPrivate

@InternalApi
class GetUserIdentifierUseCaseMock(
    identifierRepository: IdentifierRepository = IdentifierRepositoryMock()
) : GetUserIdentifierUseCase(identifierRepository) {

    var executeCalls = 0
    var resultProvider: (UserIdentifierId) -> AppResult<UserIdentifierPrivate> = { AppResult.Success(createTestUserIdentifierPrivate()) }

    override suspend fun invoke(userIdentifierId: UserIdentifierId): AppResult<UserIdentifierPrivate> {
        executeCalls++
        return resultProvider(userIdentifierId)
    }
}