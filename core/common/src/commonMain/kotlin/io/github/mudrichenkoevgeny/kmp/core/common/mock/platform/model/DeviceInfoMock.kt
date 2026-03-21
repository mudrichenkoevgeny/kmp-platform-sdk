package io.github.mudrichenkoevgeny.kmp.core.common.mock.platform.model

import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceId
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType

/**
 * Creates a deterministic mock [DeviceInfo] for previews/tests.
 *
 * @param clientType Client type to embed into the resulting [DeviceInfo].
 * @param deviceId Device id used by networking headers.
 * @param deviceName Friendly device name.
 * @param language Language code used as `Accept-Language`.
 * @param appVersion Application version string.
 * @param osVersion OS version string.
 */
fun mockDeviceInfo(
    clientType: UserClientType = UserClientType.ANDROID,
    deviceId: DeviceId = DeviceId.generate(),
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