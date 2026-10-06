package io.github.mudrichenkoevgeny.kmp.feature.user.utils

/**
 * Lightweight client-side validation helpers for auth form fields (phone, email).
 *
 * Rules are intentionally conservative UI gates; they do not replace server-side validation.
 */
object FieldValidator {

    /** Minimum inclusive length for a valid phone number (excluding symbols). */
    const val MIN_PHONE_LENGTH = 7

    /** Standard length for TOTP verification codes (RFC 6238). */
    const val TOTP_CODE_LENGTH = 6

    /** Default length for generic One-Time Passwords (SMS/Email) if not specified by server. */
    const val DEFAULT_OTP_LENGTH = 6

    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    private val PHONE_REGEX = "^\\+?[1-9]\\d{6,14}$".toRegex()
    private val PHONE_CLEANUP_REGEX = "[\\s()-]".toRegex()

    /**
     * Returns true when [phone] matches the standard E.164 format (7 to 15 digits),
     * safely ignoring visual separators like spaces, hyphens, and parentheses.
     */
    fun isPhoneNumberValid(phone: String): Boolean {
        val cleanPhone = phone.replace(PHONE_CLEANUP_REGEX, "")
        return cleanPhone.matches(PHONE_REGEX)
    }

    /**
     * Returns true when [email] matches a simple local@domain pattern (ASCII letters, digits, and a small punctuation set).
     */
    fun isEmailValid(email: String): Boolean {
        return email.matches(EMAIL_REGEX)
    }
}