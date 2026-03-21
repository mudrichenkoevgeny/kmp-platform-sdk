package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo
import io.github.mudrichenkoevgeny.kmp.core.common.repository.platform.PlatformRepository
import io.github.mudrichenkoevgeny.kmp.core.common.repository.platform.PlatformRepositoryImpl

/**
 * Internal repository wiring for platform abstractions in `core/common`.
 *
 * This module exists to bind [PlatformRepository] to a concrete implementation using
 * the provided [DeviceInfo].
 */
internal class CommonRepositoryModule(
    private val deviceInfo: DeviceInfo
) {
    /**
     * Provides access to platform/device information required by networking and websocket bootstrapping.
     */
    val platformRepository: PlatformRepository by lazy {
        PlatformRepositoryImpl(deviceInfo)
    }
}