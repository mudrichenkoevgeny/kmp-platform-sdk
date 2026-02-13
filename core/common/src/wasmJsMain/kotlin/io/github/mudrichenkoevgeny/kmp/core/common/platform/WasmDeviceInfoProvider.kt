package io.github.mudrichenkoevgeny.kmp.core.common.platform

import io.github.mudrichenkoevgeny.kmp.core.common.config.model.CommonConfig
import io.github.mudrichenkoevgeny.kmp.core.common.platform.model.DeviceInfo
import io.github.mudrichenkoevgeny.kmp.core.common.storage.common.CommonStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import kotlinx.browser.window

class WasmDeviceInfoProvider(
    private val commonConfig: CommonConfig,
    private val commonStorage: CommonStorage
) : DeviceInfoProvider {
    override suspend fun getDeviceInfo(): DeviceInfo {
        val navigator = window.navigator
        return DeviceInfo(
            clientType = UserClientType.WEB,
            deviceId = getOrCreateDeviceId(commonStorage),
            deviceName = navigator.userAgent,
            language = navigator.language,
            appVersion = commonConfig.appVersion,
            osVersion = WasmDeviceInfo.OS_VERSION
        )
    }
}

actual fun getDeviceInfoProvider(
    platformContext: Any?,
    commonConfig: CommonConfig,
    commonStorage: CommonStorage
): DeviceInfoProvider = WasmDeviceInfoProvider(commonConfig, commonStorage)

object WasmDeviceInfo {
    const val OS_VERSION = "web"
}