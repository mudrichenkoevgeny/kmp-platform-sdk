package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.DeleteUserIdentifierUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserIdentifierId

/**
 * Mock implementation of [DeleteUserIdentifierUseCase] for testing.
 */
@InternalApi
public class DeleteUserIdentifierUseCaseMock : DeleteUserIdentifierUseCase(
    identifierRepository = IdentifierRepositoryMock()
) {
    public var resultProvider: (UserIdentifierId) -> AppResult<Unit> = { AppResult.Success(Unit) }
    public var executeCalls: Int = 0

    public override suspend fun invoke(identifierId: UserIdentifierId): AppResult<Unit> {
        executeCalls++
        return resultProvider(identifierId)
    }
}
