package io.github.mudrichenkoevgeny.kmp.core.security.mock.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.repository.OpenSecuritySettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.passwordpolicy.validator.FakePasswordPolicyValidator

@InternalApi
class ValidatePasswordUseCaseMock : ValidatePasswordUseCase(
    openSecuritySettingsRepository = OpenSecuritySettingsRepositoryMock(),
    passwordPolicyValidator = FakePasswordPolicyValidator(emptyList())
) {
    var resultProvider: (String) -> AppResult<Unit> = { AppResult.Success(Unit) }

    override suspend fun invoke(password: String): AppResult<Unit> = resultProvider(password)
}
