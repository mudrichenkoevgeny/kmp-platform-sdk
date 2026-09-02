package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierEmailUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier

@InternalApi
class AddUserIdentifierEmailUseCaseMock : AddUserIdentifierEmailUseCase(
    identifierRepository = IdentifierRepositoryMock()
) {
    var resultProvider: (String, String, String) -> AppResult<UserIdentifier> = { _, _, _ ->
        AppResult.Success(userIdentifierMock())
    }
    var executeCalls = 0

    override suspend fun invoke(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifier> {
        executeCalls++
        return resultProvider(email, password, confirmationCode)
    }
}
