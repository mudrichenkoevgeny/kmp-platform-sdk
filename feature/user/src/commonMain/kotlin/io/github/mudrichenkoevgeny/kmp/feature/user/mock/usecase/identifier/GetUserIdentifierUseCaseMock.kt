package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifierUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierPrivate

/**
 * Mock implementation of [GetUserIdentifierUseCase] for testing.
 */
@InternalApi
public class GetUserIdentifierUseCaseMock(
    identifierRepository: IdentifierRepository = IdentifierRepositoryMock()
) : GetUserIdentifierUseCase(identifierRepository) {

    public var executeCalls: Int = 0
    public var resultProvider: (UserIdentifierId) -> AppResult<UserIdentifierPrivate> =
        { AppResult.Success(createTestUserIdentifierPrivate()) }

    public override suspend fun invoke(userIdentifierId: UserIdentifierId): AppResult<UserIdentifierPrivate> {
        executeCalls++
        return resultProvider(userIdentifierId)
    }
}
