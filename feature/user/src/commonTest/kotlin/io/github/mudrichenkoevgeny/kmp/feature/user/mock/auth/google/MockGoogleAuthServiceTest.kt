package io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth.google

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class MockGoogleAuthServiceTest {

    @Test
    fun signIn_success_returnsConfiguredToken() = runTest {
        val service = MockGoogleAuthService(resultToken = "custom-token", shouldSucceed = true)

        val result = service.signIn()

        assertIs<AppResult.Success<String>>(result)
        assertEquals("custom-token", result.data)
    }

    @Test
    fun signIn_failure_returnsExternalAuthFailed() = runTest {
        val service = MockGoogleAuthService(shouldSucceed = false)

        val result = service.signIn()

        assertIs<AppResult.Error>(result)
        assertTrue(result.error is UserError.ExternalAuthFailed)
    }

    @Test
    fun signOut_returnsSuccess() = runTest {
        val service = MockGoogleAuthService(shouldSucceed = false)

        val result = service.signOut()

        assertIs<AppResult.Success<Unit>>(result)
    }
}
