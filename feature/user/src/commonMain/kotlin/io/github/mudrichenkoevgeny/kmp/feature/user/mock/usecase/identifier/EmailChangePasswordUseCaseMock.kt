package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.EmailChangePasswordUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId

@InternalApi
open class EmailChangePasswordUseCaseMock : EmailChangePasswordUseCase(
    identifierRepository = IdentifierRepositoryMock()
) {
    var resultProvider: (userIdentifierId: UserIdentifierId, oldPass: String, newPass: String) -> AppResult<Unit> = { _, _, _ ->
        AppResult.Success(Unit)
    }
    var executeCalls = 0

    override suspend fun invoke(
        userIdentifierId: UserIdentifierId,
        oldPassword: String,
        newPassword: String
    ): AppResult<Unit> {
        executeCalls++
        return resultProvider(userIdentifierId, oldPassword, newPassword)
    }
}
