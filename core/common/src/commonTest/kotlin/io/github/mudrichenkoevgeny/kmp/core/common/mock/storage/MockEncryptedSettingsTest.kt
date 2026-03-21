package io.github.mudrichenkoevgeny.kmp.core.common.mock.storage

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MockEncryptedSettingsTest {

    @Test
    fun `observe emits updated values for put and remove`() = runTest {
        val settings = MockEncryptedSettings()
        val key = "device_key"
        val flow = settings.observe(key)

        assertNull(flow.first())

        settings.put(key, "value-1")
        assertEquals("value-1", flow.first())

        settings.remove(key)
        assertNull(flow.first())
    }
}
