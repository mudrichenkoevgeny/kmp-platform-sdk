package io.github.mudrichenkoevgeny.kmp.core.security.error.model

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ErrorId
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.PasswordPolicyFailReason
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.client.naming.ClientSecurityErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.naming.SecurityErrorArgs

/**
 * Security-domain [AppError] variants for password policy and related flows.
 *
 * Each instance carries a stable [code] aligned with [ClientSecurityErrorCodes] or [PasswordPolicyFailReason]
 * (or shared foundation codes where applicable for parsing).
 */
public sealed class SecurityError(
    public override val id: ErrorId,
    public override val code: String,
    public override val args: Map<String, String>? = null,
    public override val isRetryable: Boolean
) : AppError {

    /**
     * Password policy could not be loaded (for example security settings could not be fetched).
     */
    public class PasswordPolicyUnavailable : SecurityError(
        id = ErrorId.generate(),
        code = ClientSecurityErrorCodes.PASSWORD_POLICY_UNAVAILABLE,
        isRetryable = true
    )

    /**
     * Password is shorter than the minimum length required by the policy.
     *
     * @param minLength Optional minimum length requirement to populate [SecurityErrorArgs.PASSWORD_MIN_LENGTH] in [args].
     */
    public class PasswordTooShort(
        minLength: Int? = null
    ) : SecurityError(
        id = ErrorId.generate(),
        code = PasswordPolicyFailReason.TOO_SHORT.value,
        args = minLength?.let { mapOf(SecurityErrorArgs.PASSWORD_MIN_LENGTH to it.toString()) },
        isRetryable = false
    )

    /** Password does not contain a letter. */
    public class PasswordNoLetter : SecurityError(
        id = ErrorId.generate(),
        code = PasswordPolicyFailReason.NO_LETTER.value,
        isRetryable = false
    )

    /** Password does not contain an uppercase letter. */
    public class PasswordNoUpperCase : SecurityError(
        id = ErrorId.generate(),
        code = PasswordPolicyFailReason.NO_UPPERCASE.value,
        isRetryable = false
    )

    /** Password does not contain a lowercase letter. */
    public class PasswordNoLowerCase : SecurityError(
        id = ErrorId.generate(),
        code = PasswordPolicyFailReason.NO_LOWERCASE.value,
        isRetryable = false
    )

    /** Password does not contain a digit. */
    public class PasswordNoDigit : SecurityError(
        id = ErrorId.generate(),
        code = PasswordPolicyFailReason.NO_DIGIT.value,
        isRetryable = false
    )

    /** Password does not contain a special character. */
    public class PasswordNoSpecialChar : SecurityError(
        id = ErrorId.generate(),
        code = PasswordPolicyFailReason.NO_SPECIAL_CHAR.value,
        isRetryable = false
    )

    /** Password matches a blocked common-password list. */
    public class PasswordTooCommon : SecurityError(
        id = ErrorId.generate(),
        code = PasswordPolicyFailReason.TOO_COMMON.value,
        isRetryable = false
    )
}
