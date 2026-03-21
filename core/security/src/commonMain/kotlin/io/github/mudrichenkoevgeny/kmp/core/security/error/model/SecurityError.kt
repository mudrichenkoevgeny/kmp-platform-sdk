package io.github.mudrichenkoevgeny.kmp.core.security.error.model

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ErrorId
import io.github.mudrichenkoevgeny.kmp.core.security.error.naming.SecurityErrorCodes

/**
 * Security-domain [AppError] variants for password policy and related flows.
 *
 * Each instance carries a stable [code] aligned with [SecurityErrorCodes] (or shared foundation codes
 * where applicable for parsing). [args] is unused for these variants. [isRetryable] is `true` only for
 * [PasswordPolicyUnavailable] (settings fetch may succeed on retry).
 */
sealed class SecurityError(
    override val id: ErrorId,
    override val code: String,
    override val args: Map<String, String>? = null,
    override val isRetryable: Boolean
) : AppError {

    /**
     * Password policy could not be loaded (for example security settings could not be fetched).
     */
    class PasswordPolicyUnavailable : SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_POLICY_UNAVAILABLE,
        isRetryable = true
    )

    /** Password is shorter than the minimum length required by the policy. */
    class PasswordTooShort : SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_TOO_SHORT,
        isRetryable = false
    )

    /** Password does not contain a letter. */
    class PasswordNoLetter : SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_NO_LETTER,
        isRetryable = false
    )

    /** Password does not contain an uppercase letter. */
    class PasswordNoUpperCase : SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_NO_UPPERCASE,
        isRetryable = false
    )

    /** Password does not contain a lowercase letter. */
    class PasswordNoLowerCase : SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_NO_LOWERCASE,
        isRetryable = false
    )

    /** Password does not contain a digit. */
    class PasswordNoDigit : SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_NO_DIGIT,
        isRetryable = false
    )

    /** Password does not contain a special character. */
    class PasswordNoSpecialChar : SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_NO_SPECIAL_CHAR,
        isRetryable = false
    )

    /** Password matches a blocked common-password list. */
    class PasswordTooCommon : SecurityError(
        id = ErrorId.generate(),
        code = SecurityErrorCodes.PASSWORD_TOO_COMMON,
        isRetryable = false
    )
}