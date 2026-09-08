package io.github.mudrichenkoevgeny.kmp.core.security.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.error.model.SecurityError
import io.github.mudrichenkoevgeny.kmp.core.security.repository.OpenSecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.OpenPasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.PasswordPolicyFailReason
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.PasswordPolicyValidatorResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidator

/**
 * Validates a password string against the current [OpenPasswordPolicy] from [OpenSecuritySettingsRepository]
 * using [PasswordPolicyValidator].
 *
 * @param openSecuritySettingsRepository Source of the active password policy.
 * @param passwordPolicyValidator Foundation validator implementation.
 */
open class ValidatePasswordUseCase(
    private val openSecuritySettingsRepository: OpenSecuritySettingsRepository,
    private val passwordPolicyValidator: PasswordPolicyValidator
) {
    /**
     * @param password Candidate password to validate.
     * @return [AppResult.Success] when the password satisfies the policy, or [AppResult.Error] with a
     * [SecurityError] describing the first failed rule.
     */
    open suspend operator fun invoke(password: String): AppResult<Unit> {
        val securitySettingsResult = openSecuritySettingsRepository.getOpenSecuritySettings()
        val passwordPolicy = when (securitySettingsResult) {
            is AppResult.Success -> securitySettingsResult.data.passwordPolicy
            is AppResult.Error -> OpenPasswordPolicy()
        }

        val validationResult = passwordPolicyValidator.validate(
            passwordPolicy,
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
