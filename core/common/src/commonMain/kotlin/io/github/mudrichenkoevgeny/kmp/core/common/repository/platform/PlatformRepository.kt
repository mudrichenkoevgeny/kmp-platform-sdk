package io.github.mudrichenkoevgeny.kmp.core.common.repository.platform

import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo

interface PlatformRepository {
    fun getDeviceInfo(): DeviceInfo
}