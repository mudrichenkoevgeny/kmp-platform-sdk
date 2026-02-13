package io.github.mudrichenkoevgeny.kmp.core.common.mock.platform.model

import io.github.mudrichenkoevgeny.kmp.core.common.platform.model.DeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType

fun mockDeviceInfo(
    clientType: UserClientType = UserClientType.ANDROID,
    deviceId: String = "debug_device_id",
    deviceName: String = "KMP Mock Device",
    language: String = "en",
    appVersion: String = "1.0.0-debug",
    osVersion: String = "16"
) = DeviceInfo(
    clientType = clientType,
    deviceId = deviceId,
    deviceName = deviceName,
    language = language,
    appVersion = appVersion,
    osVersion = osVersion
)