package io.github.mudrichenkoevgeny.kmp.feature.user.utils

/**
 * Lightweight client-side validation helpers for auth form fields (phone, email).
 *
 * Rules are intentionally conservative UI gates; they do not replace server-side validation.
 */
object FieldValidator {

    /** Minimum inclusive length for [isPhoneNumberValid] when all characters are digits. */
    const val MIN_PHONE_LENGTH = 10

    /**
     * Returns true when [phone] contains only digits and its length is at least [MIN_PHONE_LENGTH].
     */
    fun isPhoneNumberValid(phone: String): Boolean {
        return phone.length >= MIN_PHONE_LENGTH && phone.all { it.isDigit() }
    }

    /**
     * Returns true when [email] matches a simple local@domain pattern (ASCII letters, digits, and a small punctuation set).
     */
    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return email.matches(emailRegex.toRegex())
    }
}
