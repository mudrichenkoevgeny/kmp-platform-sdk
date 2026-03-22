package io.github.mudrichenkoevgeny.kmp.feature.user.model.session

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class UserSessionIdTest {

    @Test
    fun `generate creates unique ids`() {
        val id1 = UserSessionId.generate()
        val id2 = UserSessionId.generate()

        assertNotEquals(id1, id2)
        assertNotEquals(id1.value, id2.value)
    }

    @Test
    fun `asHexDashString returns canonical hex format`() {
        val id = UserSessionId.generate()
        val hex = id.asHexDashString()

        assertEquals(HEX_DASH_UUID_LENGTH, hex.length)
        assertEquals(HEX_DASH_UUID_DASHES_COUNT, hex.count { it == '-' })
        val parsed = hex.toUserSessionIdOrThrow()
        assertEquals(id, parsed)
    }

    @Test
    fun `UserSessionId round-trips through hex string`() {
        val id = UserSessionId.generate()
        val hex = id.asHexDashString()
        val parsed = hex.toUserSessionIdOrThrow()

        assertEquals(id, parsed)
        assertEquals(id.value, parsed.value)
    }

    @Test
    fun `toUserSessionIdOrNull returns null for invalid string`() {
        assertNull(INVALID_UUID.toUserSessionIdOrNull())
        assertNull(EMPTY_UUID.toUserSessionIdOrNull())
    }

    @Test
    fun `toUserSessionIdOrNull returns UserSessionId for valid hex string`() {
        val id = UserSessionId.generate()
        val parsed = id.asHexDashString().toUserSessionIdOrNull()
        assertEquals(id, parsed)
    }

    @Test
    fun `toUserSessionIdOrThrow parses valid hex string`() {
        val id = UserSessionId.generate()
        val parsed = id.asHexDashString().toUserSessionIdOrThrow()
        assertEquals(id, parsed)
    }

    private companion object {
        const val HEX_DASH_UUID_LENGTH = 36
        const val HEX_DASH_UUID_DASHES_COUNT = 4
        const val INVALID_UUID = "not-a-uuid"
        const val EMPTY_UUID = ""
    }
}
