package io.github.mudrichenkoevgeny.kmp.feature.user.utils

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FieldValidatorTest {

    @Test
    fun isPhoneNumberValid_acceptsValidNumbers() {
        assertTrue(FieldValidator.isPhoneNumberValid(VALID_PHONE_MIN))
        assertTrue(FieldValidator.isPhoneNumberValid(VALID_PHONE_MAX))
        assertTrue(FieldValidator.isPhoneNumberValid(VALID_PHONE_WITH_PLUS))
    }

    @Test
    fun isPhoneNumberValid_acceptsVisualSeparators() {
        assertTrue(FieldValidator.isPhoneNumberValid(PHONE_WITH_SEPARATORS_1))
        assertTrue(FieldValidator.isPhoneNumberValid(PHONE_WITH_SEPARATORS_2))
    }

    @Test
    fun isPhoneNumberValid_rejectsInvalidFormats() {
        assertFalse(FieldValidator.isPhoneNumberValid(TOO_SHORT_PHONE))
        assertFalse(FieldValidator.isPhoneNumberValid(TOO_LONG_PHONE))
        assertFalse(FieldValidator.isPhoneNumberValid(PHONE_WITH_LETTER))
        assertFalse(FieldValidator.isPhoneNumberValid(PHONE_STARTS_WITH_ZERO))
    }

    @Test
    fun isEmailValid_acceptsSimpleAsciiShape() {
        assertTrue(FieldValidator.isEmailValid(VALID_EMAIL))
    }

    @Test
    fun isEmailValid_rejectsMissingAtOrDomain() {
        assertFalse(FieldValidator.isEmailValid(NO_AT))
        assertFalse(FieldValidator.isEmailValid(EMPTY))
    }

    private companion object {
        const val VALID_PHONE_MIN = "1234567" // 7 digits
        const val VALID_PHONE_MAX = "123456789012345" // 15 digits
        const val VALID_PHONE_WITH_PLUS = "+79991234567"

        const val PHONE_WITH_SEPARATORS_1 = "+7 (999) 123-45-67"
        const val PHONE_WITH_SEPARATORS_2 = "1 234 567-89-00"

        const val TOO_SHORT_PHONE = "+799912" // 6 digits
        const val TOO_LONG_PHONE = "+1234567890123456" // 16 digits
        const val PHONE_WITH_LETTER = "+7999123456a"
        const val PHONE_STARTS_WITH_ZERO = "0123456789" // E.164 requires first digit to be 1-9

        const val VALID_EMAIL = "user.name+tag@example.com"
        const val NO_AT = "not-an-email"
        const val EMPTY = ""
    }
}