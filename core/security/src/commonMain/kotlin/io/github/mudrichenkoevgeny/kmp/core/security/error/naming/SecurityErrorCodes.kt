package io.github.mudrichenkoevgeny.kmp.core.security.error.naming

object SecurityErrorCodes {
    /**
     * Password policy is missing and cannot be fetched.
     */
    const val PASSWORD_POLICY_UNAVAILABLE = "PASSWORD_POLICY_UNAVAILABLE"

    /**
     * Password does not meet the minimum length requirement.
     */
    const val PASSWORD_TOO_SHORT = "PASSWORD_TOO_SHORT"

    /**
     * Password must contain at least one letter.
     */
    const val PASSWORD_NO_LETTER = "PASSWORD_NO_LETTER"

    /**
     * Password must contain at least one uppercase letter.
     */
    const val PASSWORD_NO_UPPERCASE = "PASSWORD_NO_UPPERCASE"

    /**
     * Password must contain at least one lowercase letter.
     */
    const val PASSWORD_NO_LOWERCASE = "PASSWORD_NO_LOWERCASE"

    /**
     * Password must contain at least one digit.
     */
    const val PASSWORD_NO_DIGIT = "PASSWORD_NO_DIGIT"

    /**
     * Password must contain at least one special character.
     */
    const val PASSWORD_NO_SPECIAL_CHAR = "PASSWORD_NO_SPECIAL_CHAR"

    /**
     * Password is in the list of forbidden common passwords.
     */
    const val PASSWORD_TOO_COMMON = "PASSWORD_TOO_COMMON"
}