package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.Uuid

/**
 * Stable identifier of a device installation in the system.
 *
 * Wraps a [Uuid] to provide type safety at call sites where device identifiers are passed around,
 * logged, or serialized.
 */
@JvmInline
@Serializable
value class DeviceId(val value: Uuid) {

    /**
     * Returns the underlying [Uuid] as a canonical hex string with dashes.
     */
    fun asHexDashString(): String = value.toHexDashString()

    companion object {

        /**
         * Generates a new random [DeviceId].
         */
        fun generate() = DeviceId(Uuid.random())
    }
}

/**
 * Attempts to parse this string as a [DeviceId].
 *
 * Returns `null` if the value is not a valid UUID in hex-with-dashes form.
 */
fun String.toDeviceIdOrNull(): DeviceId? =
    Uuid.parseOrNull(this)?.let { DeviceId(it) }

/**
 * Parses this string into a [DeviceId] or throws if the value is not a valid UUID.
 */
fun String.toDeviceIdOrThrow(): DeviceId = DeviceId(Uuid.parse(this))
