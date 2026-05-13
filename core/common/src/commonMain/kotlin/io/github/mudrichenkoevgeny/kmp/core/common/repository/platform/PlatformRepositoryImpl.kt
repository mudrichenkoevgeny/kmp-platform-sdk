package io.github.mudrichenkoevgeny.kmp.core.common.repository.platform

import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo

/**
 * Default [PlatformRepository] implementation that returns a provided immutable [ClientDeviceInfo].
 */
internal class PlatformRepositoryImpl(
    private val deviceInfo: ClientDeviceInfo
) : PlatformRepository {

    override fun getDeviceInfo(): ClientDeviceInfo = deviceInfo
}