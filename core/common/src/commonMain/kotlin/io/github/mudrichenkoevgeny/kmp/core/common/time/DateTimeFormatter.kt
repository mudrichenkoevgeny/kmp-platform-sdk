package io.github.mudrichenkoevgeny.kmp.core.common.time

import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.offsetIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

/**
 * Formats an [Instant] into a human-readable date and time string.
 *
 * @param instant Instant timestamp.
 * @return Formatted date-time string, or `null` if [instant] is null.
 */
fun formatInstantToDateTime(instant: Instant?): String? {
    if (instant == null) return null
    return try {
        val timeZone = TimeZone.currentSystemDefault()
        val localDateTime = instant.toLocalDateTime(timeZone)
        val offset = instant.offsetIn(timeZone)
        val day = localDateTime.day.toString().padStart(2, '0')
        val month = (localDateTime.month.ordinal + 1).toString().padStart(2, '0')
        val year = localDateTime.year.toString()
        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')

        val offsetString = if (offset == UtcOffset.ZERO) {
            "UTC"
        } else {
            "UTC$offset"
        }

        "$day.$month.$year $hour:$minute ($offsetString)"
    } catch (_: Exception) {
        null
    }
}

/**
 * Formats epoch milliseconds into a human-readable date and time string.
 *
 * @param epochMillis Epoch milliseconds timestamp.
 * @return Formatted date-time string, or `null` if [epochMillis] is null.
 */
fun formatEpochMillisToDateTime(epochMillis: Long?): String? {
    if (epochMillis == null) return null
    return formatInstantToDateTime(Instant.fromEpochMilliseconds(epochMillis))
}

/**
 * Formats a string containing timestamp in epoch milliseconds into a human-readable date and time string.
 *
 * @param epochMillisString String representation of epoch milliseconds.
 * @return Formatted date-time string, or `null` if [epochMillisString] is null, blank, or invalid.
 */
fun formatEpochMillisToDateTime(epochMillisString: String?): String? {
    return formatEpochMillisToDateTime(epochMillisString?.toLongOrNull())
}
