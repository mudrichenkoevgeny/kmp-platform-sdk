package io.github.mudrichenkoevgeny.kmp.core.common.di

import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo
import io.github.mudrichenkoevgeny.kmp.core.common.repository.platform.PlatformRepository
import io.github.mudrichenkoevgeny.kmp.core.common.repository.platform.PlatformRepositoryImpl

internal class CommonRepositoryModule(
    private val deviceInfo: DeviceInfo
) {
    val platformRepository: PlatformRepository by lazy {
        PlatformRepositoryImpl(deviceInfo)
    }
}