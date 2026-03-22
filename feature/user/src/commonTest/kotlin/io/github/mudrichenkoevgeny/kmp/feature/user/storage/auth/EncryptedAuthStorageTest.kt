package io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth

import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.MockEncryptedSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.AccessToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.RefreshToken
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EncryptedAuthStorageTest {

    @Test
    fun init_mirrorsPersistedAccessTokenIntoFlow() = runTest {
        val settings = MockEncryptedSettings()
        settings.put(KEY_ACCESS_TOKEN, STORED_ACCESS)

        val storage = EncryptedAuthStorage(settings, this)
        advanceUntilIdle()

        assertEquals(STORED_ACCESS, storage.accessTokenFlow.value)
        assertEquals(AccessToken(STORED_ACCESS), storage.getAccessToken())
    }

    @Test
    fun updateTokens_persistsAndUpdatesFlow() = runTest {
        val storage = EncryptedAuthStorage(MockEncryptedSettings(), this)
        advanceUntilIdle()

        storage.updateTokens(AccessToken(ACCESS_A), RefreshToken(REFRESH_A), expiresAt = 99L)

        assertEquals(AccessToken(ACCESS_A), storage.getAccessToken())
        assertEquals(RefreshToken(REFRESH_A), storage.getRefreshToken())
        assertEquals(99L, storage.getExpiresAt())
        assertEquals(ACCESS_A, storage.accessTokenFlow.value)
    }

    @Test
    fun clearTokens_clearsBackingStoreAndFlow() = runTest {
        val storage = EncryptedAuthStorage(MockEncryptedSettings(), this)
        advanceUntilIdle()
        storage.updateTokens(AccessToken(ACCESS_A), RefreshToken(REFRESH_A), 1L)

        storage.clearTokens()

        assertNull(storage.getAccessToken())
        assertNull(storage.getRefreshToken())
        assertEquals(0L, storage.getExpiresAt())
        assertNull(storage.accessTokenFlow.value)
    }

    @Test
    fun authSettings_roundTrip() = runTest {
        val storage = EncryptedAuthStorage(MockEncryptedSettings(), this)
        advanceUntilIdle()
        val settings = AuthSettings(
            availableAuthProviders = AvailableAuthProviders(
                primary = emptyList(),
                secondary = emptyList()
            )
        )

        storage.updateAuthSettings(settings)

        assertEquals(settings, storage.getAuthSettings())
    }

    @Test
    fun clearAuthSettings_removesSnapshot() = runTest {
        val storage = EncryptedAuthStorage(MockEncryptedSettings(), this)
        advanceUntilIdle()
        storage.updateAuthSettings(
            AuthSettings(
                availableAuthProviders = AvailableAuthProviders(
                    primary = emptyList(),
                    secondary = emptyList()
                )
            )
        )

        storage.clearAuthSettings()

        assertNull(storage.getAuthSettings())
    }

    private companion object {
        /** Must stay aligned with [EncryptedAuthStorage] key names. */
        const val KEY_ACCESS_TOKEN = "auth_access_token"

        const val STORED_ACCESS = "preloaded-access"
        const val ACCESS_A = "access-a"
        const val REFRESH_A = "refresh-a"
    }
}
