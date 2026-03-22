package io.github.mudrichenkoevgeny.kmp.feature.user.utils

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FieldValidatorTest {

    @Test
    fun isPhoneNumberValid_acceptsDigitsWithMinLength() {
        assertTrue(FieldValidator.isPhoneNumberValid(VALID_PHONE_10))
        assertTrue(FieldValidator.isPhoneNumberValid(VALID_PHONE_11))
    }

    @Test
    fun isPhoneNumberValid_rejectsTooShortOrNonDigits() {
        assertFalse(FieldValidator.isPhoneNumberValid(TOO_SHORT_PHONE))
        assertFalse(FieldValidator.isPhoneNumberValid(PHONE_WITH_LETTER))
        assertFalse(FieldValidator.isPhoneNumberValid(PHONE_WITH_SPACE))
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
        const val VALID_PHONE_10 = "0123456789"
        const val VALID_PHONE_11 = "01234567890"
        const val TOO_SHORT_PHONE = "012345678"
        const val PHONE_WITH_LETTER = "012345678a"
        const val PHONE_WITH_SPACE = "012345678 "

        const val VALID_EMAIL = "user.name+tag@example.com"
        const val NO_AT = "not-an-email"
        const val EMPTY = ""
    }
}
