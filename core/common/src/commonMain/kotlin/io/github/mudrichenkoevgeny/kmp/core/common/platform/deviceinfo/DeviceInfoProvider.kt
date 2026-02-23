package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo

import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo

interface DeviceInfoProvider {
    fun getDeviceInfo(): DeviceInfo
}