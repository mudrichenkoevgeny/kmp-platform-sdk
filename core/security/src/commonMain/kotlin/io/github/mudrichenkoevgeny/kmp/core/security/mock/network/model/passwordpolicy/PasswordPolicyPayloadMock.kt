package io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.passwordpolicy

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.passwordpolicy.PasswordPolicyPayload

@InternalApi
fun passwordPolicyPayloadMock(
    minLength: Int = 8,
    requireLetter: Boolean = true,
    requireUpperCase: Boolean = true,
    requireLowerCase: Boolean = true,
    requireDigit: Boolean = true,
    requireSpecialChar: Boolean = true,
    commonPasswords: Set<String> = setOf("123456", "password", "qwerty")
) = PasswordPolicyPayload(
    minLength = minLength,
    requireLetter = requireLetter,
    requireUpperCase = requireUpperCase,
    requireLowerCase = requireLowerCase,
    requireDigit = requireDigit,
    requireSpecialChar = requireSpecialChar,
    commonPasswords = commonPasswords
)