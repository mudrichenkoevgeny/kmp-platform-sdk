package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.Uuid

@JvmInline
@Serializable
/**
 * Opaque identifier for a specific device.
 *
 * It is represented as UUID and provides a stable textual representation via [asHexDashString].
 */
value class DeviceId(val value: Uuid) {
    fun asHexDashString(): String = value.toHexDashString()

    companion object {
        /**
         * Generates a new random [DeviceId].
         */
        fun generate() = DeviceId(Uuid.random())
    }
}