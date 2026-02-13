package io.github.mudrichenkoevgeny.kmp.core.common.platform

import io.github.mudrichenkoevgeny.kmp.core.common.config.model.CommonConfig
import io.github.mudrichenkoevgeny.kmp.core.common.platform.model.DeviceInfo
import io.github.mudrichenkoevgeny.kmp.core.common.storage.common.CommonStorage
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface DeviceInfoProvider {
    suspend fun getDeviceInfo(): DeviceInfo
}

expect fun getDeviceInfoProvider(
    platformContext: Any?,
    commonConfig: CommonConfig,
    commonStorage: CommonStorage
): DeviceInfoProvider

@OptIn(ExperimentalUuidApi::class)
internal suspend fun getOrCreateDeviceId(commonStorage: CommonStorage): String {
    return commonStorage.getDeviceId() ?: run {
        val newId = Uuid.random().toHexDashString()
        commonStorage.updateDeviceId(newId)
        newId
    }
}