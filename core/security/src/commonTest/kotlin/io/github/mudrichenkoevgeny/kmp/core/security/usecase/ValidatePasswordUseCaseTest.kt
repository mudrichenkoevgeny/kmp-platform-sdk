package io.github.mudrichenkoevgeny.kmp.core.security.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.error.model.SecurityError
import io.github.mudrichenkoevgeny.kmp.core.security.mock.repository.OpenSecuritySettingsRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.PasswordPolicyFailReason
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.domain.model.passwordpolicy.createTestManagementPasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.domain.model.passwordpolicy.createTestOpenPasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.domain.model.securitysettings.createTestOpenSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.passwordpolicy.validator.FakePasswordPolicyValidator
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

@InternalApi
class ValidatePasswordUseCaseTest {

    @Test
    fun `should return success when validation passes`() = runTest {
        val repository = OpenSecuritySettingsRepositoryMock()
        val validator = FakePasswordPolicyValidator()
        val useCase = ValidatePasswordUseCase(repository, validator)

        val result = useCase("valid_password")

        assertTrue(result is AppResult.Success)
    }

    @Test
    fun `should return success or handle error when settings loading fails`() = runTest {
        val repository = OpenSecuritySettingsRepositoryMock(initialSettings = null).apply {
            getSecuritySettingsResult = AppResult.Error(CommonError.Unknown())
        }
        val validator = FakePasswordPolicyValidator()
        val useCase = ValidatePasswordUseCase(repository, validator)

        val result = useCase("any_password")

        assertTrue(result is AppResult.Success)
    }

    @Test
    fun `should return correct error when validation fails`() = runTest {
        val repository = OpenSecuritySettingsRepositoryMock()

        val failScenarios = mapOf(
            PasswordPolicyFailReason.TOO_SHORT to SecurityError.PasswordTooShort::class,
            PasswordPolicyFailReason.NO_LETTER to SecurityError.PasswordNoLetter::class,
            PasswordPolicyFailReason.NO_UPPERCASE to SecurityError.PasswordNoUpperCase::class,
            PasswordPolicyFailReason.NO_LOWERCASE to SecurityError.PasswordNoLowerCase::class,
            PasswordPolicyFailReason.NO_DIGIT to SecurityError.PasswordNoDigit::class,
            PasswordPolicyFailReason.NO_SPECIAL_CHAR to SecurityError.PasswordNoSpecialChar::class,
            PasswordPolicyFailReason.TOO_COMMON to SecurityError.PasswordTooCommon::class
        )

        failScenarios.forEach { (reason, expectedClass) ->
            val validator = FakePasswordPolicyValidator(failReasons = listOf(reason))
            val useCase = ValidatePasswordUseCase(repository, validator)

            val result = useCase("invalid_password")

            assertTrue(result is AppResult.Error)
            assertTrue(expectedClass.isInstance(result.error))
        }
    }
}
