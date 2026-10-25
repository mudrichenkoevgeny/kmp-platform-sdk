package io.github.mudrichenkoevgeny.kmp.feature.user.error.parser

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ErrorId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorCodes
import kotlin.test.Test
import kotlin.test.assertNotNull

class UserErrorParserTest {

    private val parser = UserErrorParser()

    @Test
    fun parse_userLocked_usesResolveUserLockedMessage() {
        val appError = object : AppError {
            override val id = ErrorId.generate()
            override val code = UserErrorCodes.USER_LOCKED
            override val args = mapOf(
                UserErrorArgs.ACCOUNT_LOCKOUT_TYPE to "TEMPORARY",
                UserErrorArgs.TEMPORARY_LOCKOUT_UNTIL to "1773325800000"
            )
            override val isRetryable = false
        }

        val parsed = parser.parse(appError)
        assertNotNull(parsed)
    }

    @Test
    fun parse_userIdentifierLimitReached_usesResolveUserIdentifierLimitReachedMessage() {
        val appError = object : AppError {
            override val id = ErrorId.generate()
            override val code = UserErrorCodes.USER_IDENTIFIER_LIMIT_REACHED
            override val args = mapOf(
                UserErrorArgs.MAX_NUMBER_OF_IDENTIFIERS to "3",
                UserErrorArgs.USER_AUTH_PROVIDER to "email"
            )
            override val isRetryable = false
        }

        val parsed = parser.parse(appError)
        assertNotNull(parsed)
    }

    @Test
    fun parse_totalUserIdentifiersLimitReached_usesResolveTotalUserIdentifiersLimitReachedMessage() {
        val appError = object : AppError {
            override val id = ErrorId.generate()
            override val code = UserErrorCodes.TOTAL_USER_IDENTIFIERS_LIMIT_REACHED
            override val args = mapOf(
                UserErrorArgs.MAX_NUMBER_OF_IDENTIFIERS to "5"
            )
            override val isRetryable = false
        }

        val parsed = parser.parse(appError)
        assertNotNull(parsed)
    }
}
