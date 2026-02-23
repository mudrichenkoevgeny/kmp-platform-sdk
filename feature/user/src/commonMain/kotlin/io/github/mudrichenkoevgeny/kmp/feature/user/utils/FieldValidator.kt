package io.github.mudrichenkoevgeny.kmp.feature.user.utils

object FieldValidator {

    const val MIN_PHONE_LENGTH = 10

    fun isPhoneNumberValid(phone: String): Boolean {
        return phone.length >= MIN_PHONE_LENGTH && phone.all { it.isDigit() }
    }

    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return email.matches(emailRegex.toRegex())
    }
}