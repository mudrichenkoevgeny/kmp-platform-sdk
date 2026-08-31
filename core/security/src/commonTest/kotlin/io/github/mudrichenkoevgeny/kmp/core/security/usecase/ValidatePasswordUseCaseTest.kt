package io.github.mudrichenkoevgeny.kmp.core.security.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.error.model.SecurityError
import io.github.mudrichenkoevgeny.kmp.core.security.repository.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.PasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.PasswordPolicyFailReason
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.PasswordPolicyValidatorResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@InternalApi
class ValidatePasswordUseCaseTest {

    @Test
    fun `should return success when validation passes`() = runTest {
        val policy = createDummyPolicy()
        val repository = FakeSecuritySettingsRepository(AppResult.Success(createSettings(policy)))
        val validator = FakePasswordPolicyValidator(PasswordPolicyValidatorResult.Success)
        val useCase = ValidatePasswordUseCase(repository, validator)

        val result = useCase("valid_password")

        assertTrue(result is AppResult.Success)
    }

    @Test
    fun `should return error when settings loading fails`() = runTest {
        val repository = FakeSecuritySettingsRepository(AppResult.Error(CommonError.Unknown()))
        val validator = FakePasswordPolicyValidator(PasswordPolicyValidatorResult.Success)
        val useCase = ValidatePasswordUseCase(repository, validator)

        val result = useCase("any_password")

        assertTrue(result is AppResult.Error)
        assertTrue(result.error is SecurityError.PasswordPolicyUnavailable)
    }

    @Test
    fun `should return correct error when validation fails`() = runTest {
        val policy = createDummyPolicy()
        val repository = FakeSecuritySettingsRepository(AppResult.Success(createSettings(policy)))
        
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
            val validator = FakePasswordPolicyValidator(
                PasswordPolicyValidatorResult.Fail(listOf(reason), policy)
            )
            val useCase = ValidatePasswordUseCase(repository, validator)

            val result = useCase("invalid_password")

            assertTrue(result is AppResult.Error)
            assertTrue(expectedClass.isInstance(result.error))
        }
    }

    private fun createSettings(policy: PasswordPolicy) = SecuritySettings(
        recentAuthenticationValiditySeconds = 300,
        recentAuthenticationValiditySecondsForManagement = 60,
        passwordPolicy = policy,
        otpConfirmation = OtpConfirmation(retryAfterSeconds = 60, numberOfSymbols = 6, expirationSeconds = 300),
        mfaTokenExpirationSeconds = 300
    )

    private fun createDummyPolicy() = PasswordPolicy(
        minLength = 8,
        requireLetter = true,
        requireUpperCase = true,
        requireLowerCase = true,
        requireDigit = true,
        requireSpecialChar = true,
        commonPasswords = emptySet()
    )

    private class FakeSecuritySettingsRepository(
        private val result: AppResult<SecuritySettings>
    ) : SecuritySettingsRepository {
        override suspend fun getSecuritySettings(): AppResult<SecuritySettings> = result
        override suspend fun refreshSecuritySettings(): AppResult<SecuritySettings> = result
        override suspend fun updateSecuritySettings(securitySettings: SecuritySettings) = Unit
        override fun observeSecuritySettings(): Flow<SecuritySettings?> = error("Not implemented")
    }

    private class FakePasswordPolicyValidator(
        private val result: PasswordPolicyValidatorResult
    ) : PasswordPolicyValidator {
        override fun validate(
            passwordPolicy: PasswordPolicy,
            password: String
        ): PasswordPolicyValidatorResult = result
    }
}
