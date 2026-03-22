package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class DeviceIdTest {

    @Test
    fun `generate creates unique ids`() {
        val id1 = DeviceId.generate()
        val id2 = DeviceId.generate()

        assertNotEquals(id1, id2)
        assertNotEquals(id1.value, id2.value)
    }

    @Test
    fun `asHexDashString returns canonical hex format`() {
        val id = DeviceId.generate()
        val hex = id.asHexDashString()

        assertEquals(HEX_DASH_UUID_LENGTH, hex.length)
        assertEquals(HEX_DASH_UUID_DASHES_COUNT, hex.count { it == '-' })
        val parsed = hex.toDeviceIdOrThrow()
        assertEquals(id, parsed)
    }

    @Test
    fun `DeviceId round-trips through hex string`() {
        val id = DeviceId.generate()
        val hex = id.asHexDashString()
        val parsed = hex.toDeviceIdOrThrow()

        assertEquals(id, parsed)
        assertEquals(id.value, parsed.value)
    }

    @Test
    fun `toDeviceIdOrNull returns null for invalid string`() {
        assertNull(INVALID_UUID.toDeviceIdOrNull())
        assertNull(EMPTY_UUID.toDeviceIdOrNull())
    }

    @Test
    fun `toDeviceIdOrNull returns DeviceId for valid hex string`() {
        val id = DeviceId.generate()
        val parsed = id.asHexDashString().toDeviceIdOrNull()
        assertEquals(id, parsed)
    }

    @Test
    fun `toDeviceIdOrThrow parses valid hex string`() {
        val id = DeviceId.generate()
        val parsed = id.asHexDashString().toDeviceIdOrThrow()
        assertEquals(id, parsed)
    }

    private companion object {
        const val HEX_DASH_UUID_LENGTH = 36
        const val HEX_DASH_UUID_DASHES_COUNT = 4
        const val INVALID_UUID = "not-a-uuid"
        const val EMPTY_UUID = ""
    }
}
