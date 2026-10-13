package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierEmailUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate

@InternalApi
class AddUserIdentifierEmailUseCaseMock(
    identifierRepository: IdentifierRepository = IdentifierRepositoryMock()
) : AddUserIdentifierEmailUseCase(identifierRepository) {

    var resultProvider: (String, String, String) -> AppResult<UserIdentifierPrivate> = { _, _, _ ->
        AppResult.Error(
            CommonError.ContractViolation(
                throwable = IllegalStateException("No mock result provided for AddUserIdentifierEmailUseCaseMock.")
            )
        )
    }

    override suspend fun invoke(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> {
        return resultProvider(email, password, confirmationCode)
    }
}