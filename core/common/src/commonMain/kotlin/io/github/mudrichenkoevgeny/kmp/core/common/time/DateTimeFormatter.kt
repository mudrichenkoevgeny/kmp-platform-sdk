package io.github.mudrichenkoevgeny.kmp.core.common.time

import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Formats epoch milliseconds into a human-readable
 * date and time string ("DD.MM.YYYY HH:mm") using the system default time zone.
 *
 * @param epochMillis Epoch milliseconds timestamp.
 * @return Formatted date-time string, or `null` if [epochMillis] is null.
 */
fun formatEpochMillisToDateTime(epochMillis: Long?): String? {
    if (epochMillis == null) return null
    return try {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val day = localDateTime.day.toString().padStart(2, '0')
        val month = (localDateTime.month.ordinal + 1).toString().padStart(2, '0')
        val year = localDateTime.year.toString()
        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')
        "$day.$month.$year $hour:$minute"
    } catch (_: Exception) {
        null
    }
}

/**
 * Formats a string containing timestamp in epoch milliseconds into a human-readable
 * date and time string ("DD.MM.YYYY HH:mm") using the system default time zone.
 *
 * @param epochMillisString String representation of epoch milliseconds (e.g. "1758452400000").
 * @return Formatted date-time string, or `null` if [epochMillisString] is null, blank, or invalid.
 */
fun formatEpochMillisToDateTime(epochMillisString: String?): String? {
    return formatEpochMillisToDateTime(epochMillisString?.toLongOrNull())
}
