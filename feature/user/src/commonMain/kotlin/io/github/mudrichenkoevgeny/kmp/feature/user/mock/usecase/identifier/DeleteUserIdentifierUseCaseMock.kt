package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.DeleteUserIdentifierUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

@InternalApi
class DeleteUserIdentifierUseCaseMock : DeleteUserIdentifierUseCase(
    identifierRepository = IdentifierRepositoryMock()
) {
    var resultProvider: (UserIdentifierId) -> AppResult<Unit> = { AppResult.Success(Unit) }
    var executeCalls = 0

    override suspend fun invoke(identifierId: UserIdentifierId): AppResult<Unit> {
        executeCalls++
        return resultProvider(identifierId)
    }
}
