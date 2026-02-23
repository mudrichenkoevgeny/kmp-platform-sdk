package io.github.mudrichenkoevgeny.kmp.core.security.error.model

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ErrorId
import io.github.mudrichenkoevgeny.kmp.core.security.error.naming.SecurityErrorCodes

sealed class SecurityError(
    override val id: ErrorId,
    override val code: String,
    override val args: Map<String, String>? = null,
    override val isRetryable: Boolean
) : AppError {

    class PasswordPolicyUnavailable: SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_POLICY_UNAVAILABLE,
        isRetryable = true
    )

    class PasswordTooShort : SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_TOO_SHORT,
        isRetryable = false
    )

    class PasswordNoLetter : SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_NO_LETTER,
        isRetryable = false
    )

    class PasswordNoUpperCase : SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_NO_UPPERCASE,
        isRetryable = false
    )

    class PasswordNoLowerCase : SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_NO_LOWERCASE,
        isRetryable = false
    )

    class PasswordNoDigit : SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_NO_DIGIT,
        isRetryable = false
    )

    class PasswordNoSpecialChar : SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_NO_SPECIAL_CHAR,
        isRetryable = false
    )

    class PasswordTooCommon : SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_TOO_COMMON,
        isRetryable = false
    )
}