package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.security.repository.OpenSecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.RefreshOpenSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidator

/**
 * Internal use-case wiring for `core/security`.
 *
 * Exposes refresh and password validation entry points built on [OpenSecuritySettingsRepository] and
 * [PasswordPolicyValidator].
 */
internal class SecurityUseCaseModule(
    openSecuritySettingsRepository: OpenSecuritySettingsRepository,
    passwordPolicyValidator: PasswordPolicyValidator
) {
    /**
     * Forces a network refresh of security settings (including password policy).
     */
    val refreshOpenSecuritySettingsUseCase by lazy {
        RefreshOpenSecuritySettingsUseCase(
            openSecuritySettingsRepository
        )
    }

    /**
     * Validates a candidate password against the current policy from [OpenSecuritySettingsRepository].
     */
    val validatePasswordUseCase by lazy {
        ValidatePasswordUseCase(
            openSecuritySettingsRepository = openSecuritySettingsRepository,
            passwordPolicyValidator = passwordPolicyValidator
        )
    }
}