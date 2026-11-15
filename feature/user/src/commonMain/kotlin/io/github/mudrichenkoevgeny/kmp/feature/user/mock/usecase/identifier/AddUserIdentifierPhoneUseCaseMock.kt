package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierPhoneUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierPrivate

/**
 * Mock implementation of [AddUserIdentifierPhoneUseCase] for testing.
 */
@InternalApi
public class AddUserIdentifierPhoneUseCaseMock(
    identifierRepository: IdentifierRepository = IdentifierRepositoryMock()
) : AddUserIdentifierPhoneUseCase(identifierRepository) {

    public var resultProvider: (String, String) -> AppResult<UserIdentifierPrivate> = { _, _ ->
        AppResult.Error(
            CommonError.ContractViolation(
                throwable = IllegalStateException("No mock result provided for AddUserIdentifierPhoneUseCaseMock.")
            )
        )
    }

    public override suspend fun invoke(
        phoneNumber: String,
        confirmationCode: String
    ): AppResult<UserIdentifierPrivate> = resultProvider(phoneNumber, confirmationCode)
}
