package io.github.mudrichenkoevgeny.kmp.feature.user.model.session

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.Uuid

/**
 * Stable identifier of an active user session row in the system.
 *
 * Wraps a [Uuid] to provide type safety at call sites where session identifiers are passed around,
 * logged, or serialized.
 */
@JvmInline
@Serializable
value class UserSessionId(val value: Uuid) {

    /**
     * Returns the underlying [Uuid] as a canonical hex string with dashes.
     */
    fun asHexDashString(): String = value.toHexDashString()

    companion object {

        /**
         * Generates a new random [UserSessionId].
         */
        fun generate() = UserSessionId(Uuid.random())
    }
}

/**
 * Attempts to parse this string as a [UserSessionId].
 *
 * Returns `null` if the value is not a valid UUID in hex-with-dashes form.
 */
fun String.toUserSessionIdOrNull(): UserSessionId? =
    Uuid.parseOrNull(this)?.let { UserSessionId(it) }

/**
 * Parses this string into a [UserSessionId] or throws if the value is not a valid UUID.
 */
fun String.toUserSessionIdOrThrow(): UserSessionId =
    UserSessionId(Uuid.parse(this))
