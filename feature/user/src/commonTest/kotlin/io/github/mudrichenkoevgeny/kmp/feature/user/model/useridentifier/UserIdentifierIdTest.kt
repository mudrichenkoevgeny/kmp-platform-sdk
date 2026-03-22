package io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class UserIdentifierIdTest {

    @Test
    fun `generate creates unique ids`() {
        val id1 = UserIdentifierId.generate()
        val id2 = UserIdentifierId.generate()

        assertNotEquals(id1, id2)
        assertNotEquals(id1.value, id2.value)
    }

    @Test
    fun `asHexDashString returns canonical hex format`() {
        val id = UserIdentifierId.generate()
        val hex = id.asHexDashString()

        assertEquals(HEX_DASH_UUID_LENGTH, hex.length)
        assertEquals(HEX_DASH_UUID_DASHES_COUNT, hex.count { it == '-' })
        val parsed = hex.toUserIdentifierIdOrThrow()
        assertEquals(id, parsed)
    }

    @Test
    fun `UserIdentifierId round-trips through hex string`() {
        val id = UserIdentifierId.generate()
        val hex = id.asHexDashString()
        val parsed = hex.toUserIdentifierIdOrThrow()

        assertEquals(id, parsed)
        assertEquals(id.value, parsed.value)
    }

    @Test
    fun `toUserIdentifierIdOrNull returns null for invalid string`() {
        assertNull(INVALID_UUID.toUserIdentifierIdOrNull())
        assertNull(EMPTY_UUID.toUserIdentifierIdOrNull())
    }

    @Test
    fun `toUserIdentifierIdOrNull returns UserIdentifierId for valid hex string`() {
        val id = UserIdentifierId.generate()
        val parsed = id.asHexDashString().toUserIdentifierIdOrNull()
        assertEquals(id, parsed)
    }

    @Test
    fun `toUserIdentifierIdOrThrow parses valid hex string`() {
        val id = UserIdentifierId.generate()
        val parsed = id.asHexDashString().toUserIdentifierIdOrThrow()
        assertEquals(id, parsed)
    }

    private companion object {
        const val HEX_DASH_UUID_LENGTH = 36
        const val HEX_DASH_UUID_DASHES_COUNT = 4
        const val INVALID_UUID = "not-a-uuid"
        const val EMPTY_UUID = ""
    }
}
