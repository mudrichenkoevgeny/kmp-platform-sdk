package io.github.mudrichenkoevgeny.kmp.core.common.storage

import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WasmSettingsFactoryTest {

    @Test
    fun `wasm encrypted settings round trip in localStorage`() = runTest {
        val key = "wasm_settings_test_key_${Random.nextInt()}"
        val settings = getSettingsFactory(null).create()

        assertNull(settings.get(key))

        settings.put(key, "stored")
        assertEquals("stored", settings.get(key))

        settings.remove(key)
        assertNull(settings.get(key))
    }
}
