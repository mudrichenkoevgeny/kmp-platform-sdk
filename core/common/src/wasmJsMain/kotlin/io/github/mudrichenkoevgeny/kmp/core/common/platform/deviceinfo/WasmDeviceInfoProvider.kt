package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo

import io.github.mudrichenkoevgeny.kmp.core.common.platform.parser.UserAgentParser
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.toClientDeviceIdOrNull
import kotlinx.browser.window

/**
 * Wasm/browser [DeviceInfoProvider]: builds [ClientDeviceInfo] from [window] `navigator` (user agent and language),
 * a host-supplied app version string, a fixed OS label from [WasmDeviceInfo], and a persistent [ClientDeviceId]
 * stored in `window.localStorage`.
 *
 * @param appVersion Version string from the embedding app or build (there is no package manager on web).
 * @param deviceIdStorageKey Key for storing the device ID in `localStorage`. Defaults to [WasmDeviceInfo.DEFAULT_DEVICE_ID_STORAGE_KEY].
 */
class WasmDeviceInfoProvider(
    private val appVersion: String,
    private val deviceIdStorageKey: String = WasmDeviceInfo.DEFAULT_DEVICE_ID_STORAGE_KEY
) : DeviceInfoProvider {

    /**
     * @return [ClientDeviceInfo] with [ClientType.WEB], a persistent [ClientDeviceId] loaded or generated from `window.localStorage`,
     * a human-readable device name parsed from `navigator.userAgent` via [UserAgentParser.getDeviceName], `navigator.language`,
     * the provided [appVersion], and [WasmDeviceInfo.OS_VERSION].
     */
    override fun getDeviceInfo(): ClientDeviceInfo {
        val navigator = window.navigator
        val deviceId = getOrCreateDeviceId()

        return ClientDeviceInfo(
            deviceId = deviceId,
            deviceName = UserAgentParser.getDeviceName(navigator.userAgent),
            clientType = ClientType.WEB,
            language = navigator.language,
            appVersion = appVersion,
            operationSystemVersion = WasmDeviceInfo.OS_VERSION
        )
    }

    private fun getOrCreateDeviceId(): ClientDeviceId {
        val storedValue = window.localStorage.getItem(deviceIdStorageKey)
        val existingDeviceId = storedValue?.toClientDeviceIdOrNull()
        if (existingDeviceId != null) {
            return existingDeviceId
        }

        val newDeviceId = ClientDeviceId.generate()
        window.localStorage.setItem(deviceIdStorageKey, newDeviceId.asHexDashString())
        return newDeviceId
    }
}

/**
 * Shared Wasm constants for [ClientDeviceInfo] fields and storage keys.
 */
object WasmDeviceInfo {
    const val OS_VERSION = "web"
    const val DEFAULT_DEVICE_ID_STORAGE_KEY = "app_device_id"
}
