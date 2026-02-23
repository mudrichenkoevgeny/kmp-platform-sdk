package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo

import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceId
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import kotlinx.browser.window

class WasmDeviceInfoProvider(
    private val appVersion: String
) : DeviceInfoProvider {
    override fun getDeviceInfo(): DeviceInfo {
        val navigator = window.navigator
        return DeviceInfo(
            clientType = UserClientType.WEB,
            deviceId = DeviceId.generate(),
            deviceName = navigator.userAgent,
            language = navigator.language,
            appVersion = appVersion,
            osVersion = WasmDeviceInfo.OS_VERSION
        )
    }
}

object WasmDeviceInfo {
    const val OS_VERSION = "web"
}