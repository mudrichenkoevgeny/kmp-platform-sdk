package io.github.mudrichenkoevgeny.kmp.core.common.platform.clientdeviceinfo

import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo

/**
 * Provides platform-specific [ClientDeviceInfo] used for SDK headers and websocket initialization.
 */
public interface ClientDeviceInfoProvider {
    /**
     * @return device metadata describing client/app/platform state.
     */
    public fun getClientDeviceInfo(): ClientDeviceInfo
}
