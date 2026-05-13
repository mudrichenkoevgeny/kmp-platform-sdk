package io.github.mudrichenkoevgeny.kmp.core.common.repository.platform

import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo

/**
 * Abstraction for accessing platform/device information from SDK layers.
 */
interface PlatformRepository {
    /**
     * @return current [ClientDeviceInfo] for the running host platform.
     */
    fun getDeviceInfo(): ClientDeviceInfo
}