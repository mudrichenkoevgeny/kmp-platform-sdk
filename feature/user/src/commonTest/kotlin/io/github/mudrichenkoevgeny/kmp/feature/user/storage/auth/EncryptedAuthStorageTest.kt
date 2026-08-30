package io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.EncryptedSettingsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.auth.settings.publicAuthSettingsMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.AccessToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.RefreshToken
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Instant

@InternalApi
class EncryptedAuthStorageTest {

    @Test
    fun init_mirrorsPersistedAccessTokenIntoFlow() = runTest {
        val settings = EncryptedSettingsMock()
        settings.put(KEY_ACCESS_TOKEN, STORED_ACCESS)

        val storage = EncryptedAuthStorage(settings, this)
        advanceUntilIdle()

        assertEquals(STORED_ACCESS, storage.accessTokenFlow.value)
        assertEquals(AccessToken(STORED_ACCESS), storage.getAccessToken())
    }

    @Test
    fun updateTokens_persistsAndUpdatesFlow() = runTest {
        val storage = EncryptedAuthStorage(EncryptedSettingsMock(), this)
        advanceUntilIdle()

        storage.updateTokens(AccessToken(ACCESS_A), RefreshToken(REFRESH_A), expiresAt = Instant.fromEpochMilliseconds(99L))

        assertEquals(AccessToken(ACCESS_A), storage.getAccessToken())
        assertEquals(RefreshToken(REFRESH_A), storage.getRefreshToken())
        assertEquals(99L, storage.getExpiresAt())
        assertEquals(ACCESS_A, storage.accessTokenFlow.value)
    }

    @Test
    fun clearTokens_clearsBackingStoreAndFlow() = runTest {
        val storage = EncryptedAuthStorage(EncryptedSettingsMock(), this)
        advanceUntilIdle()
        storage.updateTokens(AccessToken(ACCESS_A), RefreshToken(REFRESH_A), Instant.fromEpochMilliseconds(1L))

        storage.clearTokens()

        assertNull(storage.getAccessToken())
        assertNull(storage.getRefreshToken())
        assertEquals(0L, storage.getExpiresAt())
        assertNull(storage.accessTokenFlow.value)
    }

    @Test
    fun authSettings_roundTrip() = runTest {
        val storage = EncryptedAuthStorage(EncryptedSettingsMock(), this)
        advanceUntilIdle()

        val settings = publicAuthSettingsMock()

        storage.updatePublicAuthSettings(settings)

        assertEquals(settings, storage.getPublicAuthSettings())
    }

    @Test
    fun clearPublicAuthSettings_removesSnapshot() = runTest {
        val storage = EncryptedAuthStorage(EncryptedSettingsMock(), this)
        advanceUntilIdle()
        storage.updatePublicAuthSettings(publicAuthSettingsMock())

        storage.clearPublicAuthSettings()

        assertNull(storage.getPublicAuthSettings())
    }

    private companion object {
        /** Must stay aligned with [EncryptedAuthStorage] key names. */
        const val KEY_ACCESS_TOKEN = "auth_access_token"

        const val STORED_ACCESS = "preloaded-access"
        const val ACCESS_A = "access-a"
        const val REFRESH_A = "refresh-a"
    }
}
