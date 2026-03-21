package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo

import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceId
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import kotlinx.browser.window

/**
 * Wasm/browser [DeviceInfoProvider]: builds [DeviceInfo] from [window] `navigator` (user agent and language),
 * a host-supplied app version string, and a fixed OS label from [WasmDeviceInfo].
 *
 * @param appVersion Version string from the embedding app or build (there is no package manager on web).
 */
class WasmDeviceInfoProvider(
    private val appVersion: String
) : DeviceInfoProvider {
    /**
     * @return [DeviceInfo] with [UserClientType.WEB], a new [DeviceId], `navigator.userAgent` as device name,
     * `navigator.language`, the provided `appVersion`, and [WasmDeviceInfo.OS_VERSION].
     */
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

/**
 * Shared Wasm constants for [DeviceInfo] fields where the browser does not expose a real OS version API.
 */
object WasmDeviceInfo {
    const val OS_VERSION = "web"
}