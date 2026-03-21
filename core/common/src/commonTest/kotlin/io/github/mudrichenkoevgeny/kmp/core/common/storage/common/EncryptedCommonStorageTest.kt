package io.github.mudrichenkoevgeny.kmp.core.common.storage.common

import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.MockEncryptedSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EncryptedCommonStorageTest {

    @Test
    fun `getDeviceId returns null when not set`() = runTest {
        val settings = MockEncryptedSettings()
        val storage = EncryptedCommonStorage(settings)

        assertNull(storage.getDeviceId())
    }

    @Test
    fun `updateDeviceId persists and getDeviceId returns it`() = runTest {
        val settings = MockEncryptedSettings()
        val storage = EncryptedCommonStorage(settings)

        storage.updateDeviceId("device-1")

        assertEquals("device-1", storage.getDeviceId())
    }

    @Test
    fun `clear removes deviceId`() = runTest {
        val settings = MockEncryptedSettings()
        val storage = EncryptedCommonStorage(settings)

        storage.updateDeviceId("device-1")
        storage.clear()

        assertNull(storage.getDeviceId())
    }
}

