package io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.PasswordPolicy

@InternalApi
fun passwordPolicyMock(
    minLength: Int = PasswordPolicy.DEFAULT_MIN_LENGTH,
    requireLetter: Boolean = true,
    requireUpperCase: Boolean = true,
    requireLowerCase: Boolean = true,
    requireDigit: Boolean = true,
    requireSpecialChar: Boolean = true,
    commonPasswords: Set<String> = PasswordPolicy.DEFAULT_COMMON_PASSWORDS
) = PasswordPolicy(
    minLength = minLength,
    requireLetter = requireLetter,
    requireUpperCase = requireUpperCase,
    requireLowerCase = requireLowerCase,
    requireDigit = requireDigit,
    requireSpecialChar = requireSpecialChar,
    commonPasswords = commonPasswords
)