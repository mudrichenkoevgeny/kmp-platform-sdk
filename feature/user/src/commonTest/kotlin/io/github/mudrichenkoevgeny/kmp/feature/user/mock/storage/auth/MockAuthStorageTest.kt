package io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth

import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.AccessToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.RefreshToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MockAuthStorageTest {

    @Test
    fun updateTokens_persistsFieldsAndMirrorsAccessTokenFlow() = runTest {
        val storage = MockAuthStorage()
        val access = AccessToken("access-1")
        val refresh = RefreshToken("refresh-1")

        storage.updateTokens(access, refresh, expiresAt = 42_000L)

        assertEquals(access, storage.getAccessToken())
        assertEquals(refresh, storage.getRefreshToken())
        assertEquals(42_000L, storage.getExpiresAt())
        assertEquals("access-1", storage.accessTokenFlow.value)
    }

    @Test
    fun clearTokens_clearsFieldsAndFlow() = runTest {
        val storage = MockAuthStorage()
        storage.updateTokens(AccessToken("a"), RefreshToken("r"), 1L)

        storage.clearTokens()

        assertNull(storage.getAccessToken())
        assertNull(storage.getRefreshToken())
        assertEquals(0L, storage.getExpiresAt())
        assertNull(storage.accessTokenFlow.value)
    }

    @Test
    fun authSettings_roundTripAndClear() = runTest {
        val storage = MockAuthStorage()
        val settings = AuthSettings(
            availableAuthProviders = AvailableAuthProviders(
                primary = listOf(UserAuthProvider.EMAIL),
                secondary = emptyList()
            )
        )

        storage.updateAuthSettings(settings)
        assertEquals(settings, storage.getAuthSettings())

        storage.clearAuthSettings()
        assertNull(storage.getAuthSettings())
    }
}
