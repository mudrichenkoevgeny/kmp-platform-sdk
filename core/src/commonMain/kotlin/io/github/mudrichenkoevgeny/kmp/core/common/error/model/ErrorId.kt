package io.github.mudrichenkoevgeny.kmp.core.common.error.model

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

/**
 * Stable identifier used to correlate and track errors across layers.
 *
 * Wraps a [Uuid] to provide type safety at call sites where error identifiers are passed around,
 * logged, or serialized.
 */
@JvmInline
@Serializable
public value class ErrorId(public val value: Uuid) {

    /**
     * Returns the underlying [Uuid] as a canonical hex string with dashes.
     */
    public fun asHexDashString(): String = value.toHexDashString()

    public companion object {

        /**
         * Generates a new random [ErrorId].
         */
        public fun generate(): ErrorId = ErrorId(Uuid.random())
    }
}

/**
 * Attempts to parse this string as an [ErrorId].
 *
 * Returns `null` if the value is not a valid UUID in hex-with-dashes form.
 */
public fun String.toErrorIdOrNull(): ErrorId? =
    Uuid.parseOrNull(this)?.let { ErrorId(it) }

/**
 * Parses this string into an [ErrorId] or throws if the value is not a valid UUID.
 */
public fun String.toErrorIdOrThrow(): ErrorId = ErrorId(Uuid.parse(this))
