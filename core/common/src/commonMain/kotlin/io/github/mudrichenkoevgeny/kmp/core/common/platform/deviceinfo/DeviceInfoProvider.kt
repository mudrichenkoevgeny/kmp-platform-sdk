package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo

import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo

/**
 * Provides platform-specific [DeviceInfo] used for SDK headers and websocket initialization.
 */
interface DeviceInfoProvider {
    /**
     * @return device metadata describing client/app/platform state.
     */
    fun getDeviceInfo(): DeviceInfo
}