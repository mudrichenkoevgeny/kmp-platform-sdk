package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo

import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo

/**
 * Provides platform-specific [ClientDeviceInfo] used for SDK headers and websocket initialization.
 */
interface DeviceInfoProvider {
    /**
     * @return device metadata describing client/app/platform state.
     */
    fun getDeviceInfo(): ClientDeviceInfo
}