package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.EmailChangePasswordUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

/**
 * Mock implementation of [EmailChangePasswordUseCase] for testing.
 */
@InternalApi
public open class EmailChangePasswordUseCaseMock : EmailChangePasswordUseCase(
    identifierRepository = IdentifierRepositoryMock()
) {
    public var resultProvider: (
        userIdentifierId: UserIdentifierId,
        oldPass: String,
        newPass: String
    ) -> AppResult<Unit> = { _, _, _ ->
        AppResult.Success(Unit)
    }
    public var executeCalls: Int = 0

    public override suspend fun invoke(
        userIdentifierId: UserIdentifierId,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit> {
        executeCalls++
        return resultProvider(userIdentifierId, oldPassword, newPassword)
    }
}
