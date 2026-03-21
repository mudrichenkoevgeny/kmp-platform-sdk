package io.github.mudrichenkoevgeny.kmp.core.common.error.model

import kotlin.uuid.Uuid
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ErrorIdTest {

    @Test
    fun `generate creates parseable ErrorId`() {
        val id = ErrorId.generate()

        val parsed = id.asHexDashString().toErrorIdOrNull()
        requireNotNull(parsed)

        assertEquals(id.value, parsed.value)
    }

    @Test
    fun `toErrorIdOrNull returns null for invalid input`() {
        val invalid = "not-a-uuid"

        assertNull(invalid.toErrorIdOrNull())
    }

    @Test
    fun `toErrorIdOrNull parses valid uuid string`() {
        val uuid = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val input = uuid.toHexDashString()

        val parsed = input.toErrorIdOrNull()
        requireNotNull(parsed)

        assertEquals(uuid, parsed.value)
    }
}

