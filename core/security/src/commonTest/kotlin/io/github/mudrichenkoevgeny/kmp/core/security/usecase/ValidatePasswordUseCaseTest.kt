package io.github.mudrichenkoevgeny.kmp.core.security.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.error.model.SecurityError
import io.github.mudrichenkoevgeny.kmp.core.security.error.naming.SecurityErrorCodes
import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.model.PasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidatorImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

private class FakeSecuritySettingsRepository(
    var getResult: AppResult<SecuritySettings>
) : SecuritySettingsRepository {

    override suspend fun getSecuritySettings(): AppResult<SecuritySettings> = getResult

    override suspend fun refreshSecuritySettings(): AppResult<SecuritySettings> =
        AppResult.Error(CommonError.Unknown())

    override suspend fun updateSecuritySettings(securitySettings: SecuritySettings) {}

    override fun observeSecuritySettings(): Flow<SecuritySettings?> = flowOf(null)
}

class ValidatePasswordUseCaseTest {

    private val validator = PasswordPolicyValidatorImpl()

    @Test
    fun `returns success when password satisfies policy`() = runTest {
        val policy = PasswordPolicy(
            minLength = 4,
            requireLetter = false,
            requireUpperCase = false,
            requireLowerCase = false,
            requireDigit = false,
            requireSpecialChar = false,
            commonPasswords = emptySet()
        )
        val repo = FakeSecuritySettingsRepository(
            getResult = AppResult.Success(SecuritySettings(passwordPolicy = policy))
        )
        val useCase = ValidatePasswordUseCase(repo, validator)

        val r = useCase("ab12")

        assertIs<AppResult.Success<Unit>>(r)
    }

    @Test
    fun `returns password too short when policy min length not met`() = runTest {
        val policy = PasswordPolicy(
            minLength = 20,
            requireLetter = false,
            requireUpperCase = false,
            requireLowerCase = false,
            requireDigit = false,
            requireSpecialChar = false,
            commonPasswords = emptySet()
        )
        val repo = FakeSecuritySettingsRepository(
            getResult = AppResult.Success(SecuritySettings(passwordPolicy = policy))
        )
        val useCase = ValidatePasswordUseCase(repo, validator)

        val r = useCase("short")

        val err = assertIs<AppResult.Error>(r)
        assertIs<SecurityError.PasswordTooShort>(err.error)
        assertEquals(SecurityErrorCodes.PASSWORD_TOO_SHORT, err.error.code)
    }

    @Test
    fun `returns password policy unavailable when settings cannot be loaded`() = runTest {
        val repo = FakeSecuritySettingsRepository(
            getResult = AppResult.Error(CommonError.Unknown())
        )
        val useCase = ValidatePasswordUseCase(repo, validator)

        val r = useCase("any-password")

        val err = assertIs<AppResult.Error>(r)
        assertIs<SecurityError.PasswordPolicyUnavailable>(err.error)
        assertEquals(SecurityErrorCodes.PASSWORD_POLICY_UNAVAILABLE, err.error.code)
    }
}
