package io.github.mudrichenkoevgeny.kmp.core.common.repository.platform

import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo

/**
 * Abstraction for accessing platform/device information from SDK layers.
 */
interface PlatformRepository {
    /**
     * @return current [DeviceInfo] for the running host platform.
     */
    fun getDeviceInfo(): DeviceInfo
}