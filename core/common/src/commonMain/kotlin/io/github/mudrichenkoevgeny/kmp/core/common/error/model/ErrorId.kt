package io.github.mudrichenkoevgeny.kmp.core.common.error.model

import kotlin.jvm.JvmInline
import kotlin.uuid.Uuid

@JvmInline
/**
 * Identifier used to correlate and track errors across layers.
 *
 * The id is represented as UUID and can be serialized/deserialized via:
 * - [asHexDashString] for stable textual representation
 * - [toErrorIdOrNull] for safe parsing from external strings
 */
value class ErrorId(val value: Uuid) {
    fun asHexDashString(): String = value.toHexDashString()

    companion object {
        /**
         * Generates a new random [ErrorId].
         */
        fun generate() = ErrorId(Uuid.random())
    }
}

/**
 * Parses a server/external string into [ErrorId].
 *
 * @return parsed [ErrorId] or `null` if the input is not a valid UUID.
 */
fun String.toErrorIdOrNull(): ErrorId? =
    Uuid.parseOrNull(this)?.let { ErrorId(it) }