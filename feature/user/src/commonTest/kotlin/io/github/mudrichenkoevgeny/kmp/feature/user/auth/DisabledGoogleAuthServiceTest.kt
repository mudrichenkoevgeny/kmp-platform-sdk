package io.github.mudrichenkoevgeny.kmp.feature.user.auth

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.DisabledGoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DisabledGoogleAuthServiceTest {

    @Test
    fun signIn_returnsExternalAuthFailed() = runTest {
        val result = DisabledGoogleAuthService().signIn()

        assertTrue(result is AppResult.Error)
        val error = result.error
        assertTrue(error is UserError.ExternalAuthFailed)
        assertEquals("Google Auth is not supported", error.throwable?.message)
    }

    @Test
    fun signOut_returnsExternalAuthFailed() = runTest {
        val result = DisabledGoogleAuthService().signOut()

        assertTrue(result is AppResult.Error)
        val error = result.error
        assertTrue(error is UserError.ExternalAuthFailed)
    }
}
