package io.github.mudrichenkoevgeny.kmp.core.common.time

import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DateTimeFormatterTest {

    @Test
    fun formatEpochMillisToDateTime_returnsNullWhenNullOrBlankOrInvalid() {
        assertNull(formatEpochMillisToDateTime(null as String?))
        assertNull(formatEpochMillisToDateTime(null as Long?))
        assertNull(formatEpochMillisToDateTime(""))
        assertNull(formatEpochMillisToDateTime("invalid-millis"))
    }

    @Test
    fun formatEpochMillisToDateTime_formatsValidEpochMillis() {
        val result = formatEpochMillisToDateTime("1758452400000")
        assertTrue((result != null) && (result.length >= 16))
    }
}
