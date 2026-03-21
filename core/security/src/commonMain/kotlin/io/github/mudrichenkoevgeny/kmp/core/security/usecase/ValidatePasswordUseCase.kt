package io.github.mudrichenkoevgeny.kmp.core.security.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.error.model.SecurityError
import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.model.PasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.model.PasswordPolicyFailReason
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.model.PasswordPolicyValidatorResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidator

/**
 * Validates a password string against the current [PasswordPolicy] from [SecuritySettingsRepository]
 * using [PasswordPolicyValidator].
 *
 * @param securitySettingsRepository Source of the active password policy.
 * @param passwordPolicyValidator Foundation validator implementation.
 */
class ValidatePasswordUseCase(
    private val securitySettingsRepository: SecuritySettingsRepository,
    private val passwordPolicyValidator: PasswordPolicyValidator
) {
    /**
     * @param password Candidate password to validate.
     * @return [AppResult.Success] when the password satisfies the policy, or [AppResult.Error] with a
     * [SecurityError] describing the first failed rule. If settings cannot be loaded, returns
     * [SecurityError.PasswordPolicyUnavailable].
     */
    suspend operator fun invoke(password: String): AppResult<Unit> {
        val securitySettingsResult = securitySettingsRepository.getSecuritySettings()

        val securitySettings = when (securitySettingsResult) {
            is AppResult.Error -> return AppResult.Error(SecurityError.PasswordPolicyUnavailable())
            is AppResult.Success -> securitySettingsResult.data
        }
        val validationResult = passwordPolicyValidator.validate(
            securitySettings.passwordPolicy,
            password
        )
        return when (validationResult) {
            is PasswordPolicyValidatorResult.Success -> AppResult.Success(Unit)
            is PasswordPolicyValidatorResult.Fail -> {
                val primaryReason = validationResult.reasons.first()
                AppResult.Error(primaryReason.toSecurityError())
            }
        }
    }

    private fun PasswordPolicyFailReason.toSecurityError(): SecurityError = when (this) {
        PasswordPolicyFailReason.TOO_SHORT -> SecurityError.PasswordTooShort()
        PasswordPolicyFailReason.NO_LETTER -> SecurityError.PasswordNoLetter()
        PasswordPolicyFailReason.NO_UPPERCASE -> SecurityError.PasswordNoUpperCase()
        PasswordPolicyFailReason.NO_LOWERCASE -> SecurityError.PasswordNoLowerCase()
        PasswordPolicyFailReason.NO_DIGIT -> SecurityError.PasswordNoDigit()
        PasswordPolicyFailReason.NO_SPECIAL_CHAR -> SecurityError.PasswordNoSpecialChar()
        PasswordPolicyFailReason.TOO_COMMON -> SecurityError.PasswordTooCommon()
    }
}