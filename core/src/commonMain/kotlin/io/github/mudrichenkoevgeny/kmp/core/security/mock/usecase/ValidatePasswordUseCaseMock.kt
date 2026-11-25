package io.github.mudrichenkoevgeny.kmp.core.security.mock.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.repository.OpenSecuritySettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.passwordpolicy.validator.PasswordPolicyValidatorMock

/**
 * Mock implementation of [ValidatePasswordUseCase] for testing purposes.
 */
@InternalApi
public class ValidatePasswordUseCaseMock : ValidatePasswordUseCase(
    openSecuritySettingsRepository = OpenSecuritySettingsRepositoryMock(),
    passwordPolicyValidator = PasswordPolicyValidatorMock()
) {
    public var resultProvider: (String) -> AppResult<Unit> = { AppResult.Success(Unit) }

    public override suspend fun invoke(password: String): AppResult<Unit> = resultProvider(password)
}
