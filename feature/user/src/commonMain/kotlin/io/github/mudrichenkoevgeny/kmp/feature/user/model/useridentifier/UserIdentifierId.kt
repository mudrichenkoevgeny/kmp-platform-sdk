package io.github.mudrichenkoevgeny.kmp.feature.user.model.useridentifier

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.Uuid

/**
 * Stable identifier of a user login identifier row (email, phone, or external provider linkage) in the system.
 *
 * Wraps a [Uuid] to provide type safety at call sites where these identifiers are passed around,
 * logged, or serialized.
 */
@JvmInline
@Serializable
value class UserIdentifierId(val value: Uuid) {

    /**
     * Returns the underlying [Uuid] as a canonical hex string with dashes.
     */
    fun asHexDashString(): String = value.toHexDashString()

    companion object {

        /**
         * Generates a new random [UserIdentifierId].
         */
        fun generate() = UserIdentifierId(Uuid.random())
    }
}

/**
 * Attempts to parse this string as a [UserIdentifierId].
 *
 * Returns `null` if the value is not a valid UUID in hex-with-dashes form.
 */
fun String.toUserIdentifierIdOrNull(): UserIdentifierId? =
    Uuid.parseOrNull(this)?.let { UserIdentifierId(it) }

/**
 * Parses this string into a [UserIdentifierId] or throws if the value is not a valid UUID.
 */
fun String.toUserIdentifierIdOrThrow(): UserIdentifierId =
    UserIdentifierId(Uuid.parse(this))
