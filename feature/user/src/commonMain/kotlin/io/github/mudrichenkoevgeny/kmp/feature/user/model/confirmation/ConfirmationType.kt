package io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation

/** Which confirmation flow a [ConfirmationKey] refers to (registration, password reset, device add, etc.). */
enum class ConfirmationType {
    /** Confirmation for a new user registration via email. */
    REGISTRATION_EMAIL,

    /** Confirmation for adding a new email address to an existing account. */
    ADD_EMAIL,

    /** Confirmation for a password reset request via email. */
    PASSWORD_RESET_EMAIL,

    /** Confirmation for a login attempt via phone (OTP). */
    LOGIN_PHONE,

    /** Confirmation for adding a new phone number to an existing account. */
    ADD_PHONE
}