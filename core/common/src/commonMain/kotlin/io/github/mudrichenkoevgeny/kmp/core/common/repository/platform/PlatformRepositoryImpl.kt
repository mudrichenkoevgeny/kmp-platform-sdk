package io.github.mudrichenkoevgeny.kmp.core.common.repository.platform

import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo

/**
 * Default [PlatformRepository] implementation that returns a provided immutable [ClientDeviceInfo].
 */
internal class PlatformRepositoryImpl(
    private val clientDeviceInfo: ClientDeviceInfo
) : PlatformRepository {

    override fun getClientDeviceInfo(): ClientDeviceInfo = clientDeviceInfo
}