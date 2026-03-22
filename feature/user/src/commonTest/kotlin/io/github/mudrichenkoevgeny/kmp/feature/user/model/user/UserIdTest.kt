package io.github.mudrichenkoevgeny.kmp.feature.user.model.user

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class UserIdTest {

    @Test
    fun `generate creates unique ids`() {
        val id1 = UserId.generate()
        val id2 = UserId.generate()

        assertNotEquals(id1, id2)
        assertNotEquals(id1.value, id2.value)
    }

    @Test
    fun `asHexDashString returns canonical hex format`() {
        val id = UserId.generate()
        val hex = id.asHexDashString()

        assertEquals(HEX_DASH_UUID_LENGTH, hex.length)
        assertEquals(HEX_DASH_UUID_DASHES_COUNT, hex.count { it == '-' })
        val parsed = hex.toUserIdOrThrow()
        assertEquals(id, parsed)
    }

    @Test
    fun `UserId round-trips through hex string`() {
        val id = UserId.generate()
        val hex = id.asHexDashString()
        val parsed = hex.toUserIdOrThrow()

        assertEquals(id, parsed)
        assertEquals(id.value, parsed.value)
    }

    @Test
    fun `toUserIdOrNull returns null for invalid string`() {
        assertNull(INVALID_UUID.toUserIdOrNull())
        assertNull(EMPTY_UUID.toUserIdOrNull())
    }

    @Test
    fun `toUserIdOrNull returns UserId for valid hex string`() {
        val id = UserId.generate()
        val parsed = id.asHexDashString().toUserIdOrNull()
        assertEquals(id, parsed)
    }

    @Test
    fun `toUserIdOrThrow parses valid hex string`() {
        val id = UserId.generate()
        val parsed = id.asHexDashString().toUserIdOrThrow()
        assertEquals(id, parsed)
    }

    private companion object {
        const val HEX_DASH_UUID_LENGTH = 36
        const val HEX_DASH_UUID_DASHES_COUNT = 4
        const val INVALID_UUID = "not-a-uuid"
        const val EMPTY_UUID = ""
    }
}
