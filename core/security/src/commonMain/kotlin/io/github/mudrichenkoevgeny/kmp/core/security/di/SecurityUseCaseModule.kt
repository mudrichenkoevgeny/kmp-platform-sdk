package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.RefreshSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidator

internal class SecurityUseCaseModule(
    securitySettingsRepository: SecuritySettingsRepository,
    passwordPolicyValidator: PasswordPolicyValidator
) {
    val refreshSecuritySettingsUseCase by lazy {
        RefreshSecuritySettingsUseCase(
            securitySettingsRepository
        )
    }

    val validatePasswordUseCase by lazy {
        ValidatePasswordUseCase(
            securitySettingsRepository = securitySettingsRepository,
            passwordPolicyValidator = passwordPolicyValidator
        )
    }
}