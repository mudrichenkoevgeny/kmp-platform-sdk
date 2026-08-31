package io.github.mudrichenkoevgeny.kmp.core.security.mock.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.passwordpolicy.validator.PasswordPolicyValidatorMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.repository.SecuritySettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase

@InternalApi
class ValidatePasswordUseCaseMock : ValidatePasswordUseCase(
    securitySettingsRepository = SecuritySettingsRepositoryMock(),
    passwordPolicyValidator = PasswordPolicyValidatorMock()
) {
    var resultProvider: (String) -> AppResult<Unit> = { AppResult.Success(Unit) }

    override suspend fun invoke(password: String): AppResult<Unit> = resultProvider(password)
}
