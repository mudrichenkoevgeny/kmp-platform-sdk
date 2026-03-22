package io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation

/** Which confirmation flow a [ConfirmationKey] refers to (registration, password reset, device add, etc.). */
enum class ConfirmationType {
    REGISTRATION_EMAIL,
    ADD_EMAIL,
    PASSWORD_RESET_EMAIL,
    LOGIN_PHONE,
    ADD_PHONE
}