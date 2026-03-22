package io.github.mudrichenkoevgeny.kmp.core.common.error.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class ErrorIdTest {

    @Test
    fun `generate creates unique ids`() {
        val id1 = ErrorId.generate()
        val id2 = ErrorId.generate()

        assertNotEquals(id1, id2)
        assertNotEquals(id1.value, id2.value)
    }

    @Test
    fun `asHexDashString returns canonical hex format`() {
        val id = ErrorId.generate()
        val hex = id.asHexDashString()

        assertEquals(HEX_DASH_UUID_LENGTH, hex.length)
        assertEquals(HEX_DASH_UUID_DASHES_COUNT, hex.count { it == '-' })
        val parsed = hex.toErrorIdOrThrow()
        assertEquals(id, parsed)
    }

    @Test
    fun `ErrorId round-trips through hex string`() {
        val id = ErrorId.generate()
        val hex = id.asHexDashString()
        val parsed = hex.toErrorIdOrThrow()

        assertEquals(id, parsed)
        assertEquals(id.value, parsed.value)
    }

    @Test
    fun `toErrorIdOrNull returns null for invalid string`() {
        assertNull(INVALID_UUID.toErrorIdOrNull())
        assertNull(EMPTY_UUID.toErrorIdOrNull())
    }

    @Test
    fun `toErrorIdOrNull returns ErrorId for valid hex string`() {
        val id = ErrorId.generate()
        val parsed = id.asHexDashString().toErrorIdOrNull()
        assertEquals(id, parsed)
    }

    @Test
    fun `toErrorIdOrThrow parses valid hex string`() {
        val id = ErrorId.generate()
        val parsed = id.asHexDashString().toErrorIdOrThrow()
        assertEquals(id, parsed)
    }

    private companion object {
        const val HEX_DASH_UUID_LENGTH = 36
        const val HEX_DASH_UUID_DASHES_COUNT = 4
        const val INVALID_UUID = "not-a-uuid"
        const val EMPTY_UUID = ""
    }
}
