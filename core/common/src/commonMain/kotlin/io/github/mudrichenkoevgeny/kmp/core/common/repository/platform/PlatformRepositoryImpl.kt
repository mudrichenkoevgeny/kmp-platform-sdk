package io.github.mudrichenkoevgeny.kmp.core.common.repository.platform

import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo

/**
 * Default [PlatformRepository] implementation that returns a provided immutable [DeviceInfo].
 */
internal class PlatformRepositoryImpl(
    private val deviceInfo: DeviceInfo
) : PlatformRepository {

    override fun getDeviceInfo(): DeviceInfo = deviceInfo
}