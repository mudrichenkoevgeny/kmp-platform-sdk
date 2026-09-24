package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifierUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

@InternalApi
class GetUserIdentifierUseCaseMock : GetUserIdentifierUseCase(
    identifierRepository = IdentifierRepositoryMock()
) {
    var resultProvider: (UserIdentifierId) -> AppResult<UserIdentifier> = { AppResult.Success(userIdentifierMock()) }
    var executeCalls = 0

    override suspend fun invoke(userIdentifierId: UserIdentifierId): AppResult<UserIdentifier> {
        executeCalls++
        return resultProvider(userIdentifierId)
    }
}
