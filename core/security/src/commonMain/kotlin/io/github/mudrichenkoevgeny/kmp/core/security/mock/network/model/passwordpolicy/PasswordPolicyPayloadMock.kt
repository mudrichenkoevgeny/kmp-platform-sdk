package io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.passwordpolicy

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.ManagementPasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.passwordpolicy.ManagementPasswordPolicyPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.passwordpolicy.OpenPasswordPolicyPayload

@InternalApi
fun openPasswordPolicyPayloadMock(
    minLength: Int = ManagementPasswordPolicy.DEFAULT_MIN_LENGTH,
    requireLetter: Boolean = true,
    requireUpperCase: Boolean = true,
    requireLowerCase: Boolean = true,
    requireDigit: Boolean = true,
    requireSpecialChar: Boolean = true
) = OpenPasswordPolicyPayload(
    minLength = minLength,
    requireLetter = requireLetter,
    requireUpperCase = requireUpperCase,
    requireLowerCase = requireLowerCase,
    requireDigit = requireDigit,
    requireSpecialChar = requireSpecialChar
)

@InternalApi
fun managementPasswordPolicyPayloadMock(
    minLength: Int = ManagementPasswordPolicy.DEFAULT_MIN_LENGTH,
    requireLetter: Boolean = true,
    requireUpperCase: Boolean = true,
    requireLowerCase: Boolean = true,
    requireDigit: Boolean = true,
    requireSpecialChar: Boolean = true,
    commonPasswords: Set<String> = ManagementPasswordPolicy.DEFAULT_COMMON_PASSWORDS
) = ManagementPasswordPolicyPayload(
    minLength = minLength,
    requireLetter = requireLetter,
    requireUpperCase = requireUpperCase,
    requireLowerCase = requireLowerCase,
    requireDigit = requireDigit,
    requireSpecialChar = requireSpecialChar,
    commonPasswords = commonPasswords
)
