package io.github.mudrichenkoevgeny.kmp.core.security.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.flatMapSuccess
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.security.error.model.SecurityError
import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.model.PasswordPolicyFailReason
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.model.PasswordPolicyValidatorResult
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidator

class ValidatePasswordUseCase(
    private val securitySettingsRepository: SecuritySettingsRepository,
    private val passwordPolicyValidator: PasswordPolicyValidator
) {
    suspend operator fun invoke(password: String): AppResult<Unit> {
        return securitySettingsRepository.getSecuritySettings()
            .flatMapSuccess { securitySettings ->
                val validationResult = passwordPolicyValidator.validate(
                    securitySettings.passwordPolicy,
                    password
                )

                when (validationResult) {
                    is PasswordPolicyValidatorResult.Success -> AppResult.Success(Unit)
                    is PasswordPolicyValidatorResult.Fail -> {
                        val primaryReason = validationResult.reasons.first()
                        AppResult.Error(primaryReason.toSecurityError())
                    }
                }
            }
            .onError {
                return AppResult.Error(SecurityError.PasswordPolicyUnavailable())
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