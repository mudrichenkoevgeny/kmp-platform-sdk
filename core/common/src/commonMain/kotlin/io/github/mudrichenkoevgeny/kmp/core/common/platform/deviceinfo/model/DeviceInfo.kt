package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model

import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType

data class DeviceInfo(
    val clientType: UserClientType,
    val deviceId: DeviceId,
    val deviceName: String,
    val language: String,
    val appVersion: String,
    val osVersion: String
) {

    fun isMobileClient(): Boolean = clientType == UserClientType.ANDROID
            || clientType == UserClientType.IOS

    companion object {
        const val VERSION_UNKNOWN = "unknown"
    }
}