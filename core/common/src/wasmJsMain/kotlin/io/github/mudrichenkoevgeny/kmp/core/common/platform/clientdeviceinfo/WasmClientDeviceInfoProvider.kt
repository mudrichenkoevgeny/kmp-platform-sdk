package io.github.mudrichenkoevgeny.kmp.core.common.platform.clientdeviceinfo

import io.github.mudrichenkoevgeny.kmp.core.common.platform.parser.UserAgentParser
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.toClientDeviceIdOrNull
import kotlinx.browser.window

/**
 * Wasm/browser [ClientDeviceInfoProvider]: builds [ClientDeviceInfo] from [window] `navigator` (user agent and language),
 * a host-supplied app version string, a fixed OS label from [OS_VERSION], and a persistent [ClientDeviceId]
 * stored in `window.localStorage`.
 *
 * @param appVersion Version string from the embedding app or build (there is no package manager on web).
 * @param deviceIdStorageKey Key for storing the device ID in `localStorage`. Defaults to [DEFAULT_DEVICE_ID_STORAGE_KEY].
 */
public class WasmClientDeviceInfoProvider(
    private val appVersion: String,
    private val deviceIdStorageKey: String = DEFAULT_DEVICE_ID_STORAGE_KEY
) : ClientDeviceInfoProvider {

    /**
     * @return [ClientDeviceInfo] with [ClientType.WEB], a persistent [ClientDeviceId] loaded or generated from `window.localStorage`,
     * a human-readable device name parsed from `navigator.userAgent` via [UserAgentParser.getDeviceName], `navigator.language`,
     * the provided [appVersion], and [OS_VERSION].
     */
    public override fun getClientDeviceInfo(): ClientDeviceInfo {
        val navigator = window.navigator
        val deviceId = getOrCreateDeviceId()

        return ClientDeviceInfo(
            deviceId = deviceId,
            deviceName = UserAgentParser.getDeviceName(navigator.userAgent),
            clientType = ClientType.WEB,
            language = ClientDeviceInfo.parsePrimaryLanguage(navigator.language),
            appVersion = appVersion,
            operationSystemVersion = OS_VERSION
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

    public companion object {
        /**
         * Operating system version for Web target.
         */
        public const val OS_VERSION: String = "web"

        /**
         * Default key used for storing device ID in browser's local storage.
         */
        public const val DEFAULT_DEVICE_ID_STORAGE_KEY: String = "app_device_id"
    }
}
