package io.github.mudrichenkoevgeny.kmp.core.security.error.parser

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.ErrorId
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.naming.SecurityErrorArgs
import io.github.mudrichenkoevgeny.shared.foundation.core.security.error.naming.SecurityErrorCodes
import kotlin.test.Test
import kotlin.test.assertNotNull

class SecurityErrorParserTest {

    private val parser = SecurityErrorParser()

    @Test
    fun parse_passwordTooWeak_usesResolvePasswordTooWeakReasons() {
        val appError = object : AppError {
            override val id = ErrorId.generate()
            override val code = SecurityErrorCodes.PASSWORD_TOO_WEAK
            override val args = mapOf(
                SecurityErrorArgs.PASSWORD_FAIL_TOO_SHORT to "true",
                SecurityErrorArgs.PASSWORD_MIN_LENGTH to "8"
            )
            override val isRetryable = false
        }

        val parsed = parser.parse(appError)
        assertNotNull(parsed)
    }
}
