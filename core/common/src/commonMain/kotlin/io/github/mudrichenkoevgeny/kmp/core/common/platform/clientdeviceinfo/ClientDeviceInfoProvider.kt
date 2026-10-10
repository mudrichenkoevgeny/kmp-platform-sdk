package io.github.mudrichenkoevgeny.kmp.core.common.platform.clientdeviceinfo

import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo

/**
 * Provides platform-specific [ClientDeviceInfo] used for SDK headers and websocket initialization.
 */
interface ClientDeviceInfoProvider {
    /**
     * @return device metadata describing client/app/platform state.
     */
    fun getClientDeviceInfo(): ClientDeviceInfo
}