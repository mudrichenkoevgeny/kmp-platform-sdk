package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.repository.platform.PlatformRepository
import io.github.mudrichenkoevgeny.kmp.core.common.repository.platform.PlatformRepositoryImpl
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo

/**
 * Internal repository wiring for platform abstractions in `core/common`.
 *
 * This module exists to bind [PlatformRepository] to a concrete implementation using
 * the provided [ClientDeviceInfo].
 */
internal class CommonRepositoryModule(
    private val deviceInfo: ClientDeviceInfo
) {
    /**
     * Provides access to platform/device information required by networking and websocket bootstrapping.
     */
    val platformRepository: PlatformRepository by lazy {
        PlatformRepositoryImpl(deviceInfo)
    }
}