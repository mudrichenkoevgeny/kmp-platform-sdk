package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.RefreshSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidator

/**
 * Internal use-case wiring for `core/security`.
 *
 * Exposes refresh and password validation entry points built on [SecuritySettingsRepository] and
 * [PasswordPolicyValidator].
 */
internal class SecurityUseCaseModule(
    securitySettingsRepository: SecuritySettingsRepository,
    passwordPolicyValidator: PasswordPolicyValidator
) {
    /**
     * Forces a network refresh of security settings (including password policy).
     */
    val refreshSecuritySettingsUseCase by lazy {
        RefreshSecuritySettingsUseCase(
            securitySettingsRepository
        )
    }

    /**
     * Validates a candidate password against the current policy from [SecuritySettingsRepository].
     */
    val validatePasswordUseCase by lazy {
        ValidatePasswordUseCase(
            securitySettingsRepository = securitySettingsRepository,
            passwordPolicyValidator = passwordPolicyValidator
        )
    }
}