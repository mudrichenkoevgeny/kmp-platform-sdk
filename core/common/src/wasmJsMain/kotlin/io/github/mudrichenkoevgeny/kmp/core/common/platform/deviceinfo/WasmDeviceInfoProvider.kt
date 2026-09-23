package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo

import io.github.mudrichenkoevgeny.kmp.core.common.platform.parser.UserAgentParser
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import kotlinx.browser.window

/**
 * Wasm/browser [DeviceInfoProvider]: builds [ClientDeviceInfo] from [window] `navigator` (user agent and language),
 * a host-supplied app version string, and a fixed OS label from [WasmDeviceInfo].
 *
 * @param appVersion Version string from the embedding app or build (there is no package manager on web).
 */
class WasmDeviceInfoProvider(
    private val appVersion: String
) : DeviceInfoProvider {
    /**
     * @return [ClientDeviceInfo] with [ClientType.WEB], a new [ClientDeviceId], a human-readable device name parsed from
     * `navigator.userAgent` via [UserAgentParser.getDeviceName], `navigator.language`, the provided [appVersion], and
     * [WasmDeviceInfo.OS_VERSION].
     */
    override fun getDeviceInfo(): ClientDeviceInfo {
        val navigator = window.navigator
        return ClientDeviceInfo(
            deviceId = ClientDeviceId.generate(),
            deviceName = UserAgentParser.getDeviceName(navigator.userAgent),
            clientType = ClientType.WEB,
            language = navigator.language,
            appVersion = appVersion,
            operationSystemVersion = WasmDeviceInfo.OS_VERSION
        )
    }
}

/**
 * Shared Wasm constants for [ClientDeviceInfo] fields where the browser does not expose a real OS version API.
 */
object WasmDeviceInfo {
    const val OS_VERSION = "web"
}
