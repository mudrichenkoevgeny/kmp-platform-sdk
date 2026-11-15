package io.github.mudrichenkoevgeny.kmp.feature.user.utils

/**
 * Lightweight client-side validation helpers for auth form fields (phone, email).
 *
 * Rules are intentionally conservative UI gates; they do not replace server-side validation.
 */
public object FieldValidator {

    /** Minimum inclusive length for a valid phone number (excluding symbols). */
    public const val MIN_PHONE_LENGTH: Int = 7

    /** Standard length for TOTP verification codes (RFC 6238). */
    public const val TOTP_CODE_LENGTH: Int = 6

    /** Default length for generic One-Time Passwords (SMS/Email) if not specified by server. */
    public const val DEFAULT_OTP_LENGTH: Int = 6

    private val EMAIL_REGEX: Regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    private val PHONE_REGEX: Regex = "^\\+?[1-9]\\d{6,14}$".toRegex()
    private val PHONE_CLEANUP_REGEX: Regex = "[\\s()-]".toRegex()

    /**
     * Returns true when [phone] matches the standard E.164 format (7 to 15 digits),
     * safely ignoring visual separators like spaces, hyphens, and parentheses.
     */
    public fun isPhoneNumberValid(phone: String): Boolean {
        val cleanPhone = phone.replace(PHONE_CLEANUP_REGEX, "")
        return cleanPhone.matches(PHONE_REGEX)
    }

    /**
     * Returns true when [email] matches a simple local@domain pattern (ASCII letters, digits, and a small punctuation set).
     */
    public fun isEmailValid(email: String): Boolean = email.matches(EMAIL_REGEX)
}
