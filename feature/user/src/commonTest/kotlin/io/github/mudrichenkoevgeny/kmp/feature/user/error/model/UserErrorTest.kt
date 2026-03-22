package io.github.mudrichenkoevgeny.kmp.feature.user.error.model

import io.github.mudrichenkoevgeny.kmp.feature.user.error.naming.ClientUserErrorCodes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.error.naming.UserErrorCodes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame

class UserErrorTest {

    @Test
    fun invalidRefreshToken_usesServerCode_andNotRetryable() {
        val error = UserError.InvalidRefreshToken()

        assertEquals(UserErrorCodes.INVALID_REFRESH_TOKEN, error.code)
        assertFalse(error.isRetryable)
        assertNull(error.args)
    }

    @Test
    fun externalAuthCancelled_preservesThrowable_andCode() {
        val cause = IllegalStateException("cancelled")
        val error = UserError.ExternalAuthCancelled(cause)

        assertEquals(ClientUserErrorCodes.EXTERNAL_AUTH_CANCELLED, error.code)
        assertSame(cause, error.throwable)
        assertFalse(error.isRetryable)
    }

    @Test
    fun externalAuthFailed_preservesThrowable_andCode() {
        val cause = RuntimeException("failed")
        val error = UserError.ExternalAuthFailed(cause)

        assertEquals(ClientUserErrorCodes.EXTERNAL_AUTH_FAILED, error.code)
        assertSame(cause, error.throwable)
        assertFalse(error.isRetryable)
    }

    @Test
    fun tooManyConfirmationRequests_exposesRetryDelay() {
        val error = UserError.TooManyConfirmationRequests(retryAfterSeconds = 30)

        assertEquals(ClientUserErrorCodes.TOO_MANY_CONFIRMATION_REQUESTS, error.code)
        assertEquals(30, error.retryAfterSeconds)
        assertFalse(error.isRetryable)
    }
}
