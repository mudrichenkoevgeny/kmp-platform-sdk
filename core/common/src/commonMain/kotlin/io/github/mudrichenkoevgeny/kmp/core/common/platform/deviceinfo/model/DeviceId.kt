package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@JvmInline
@Serializable
value class DeviceId(val value: Uuid) {
    fun asHexDashString(): String = value.toHexDashString()

    companion object {
        fun generate() = DeviceId(Uuid.random())
    }
}