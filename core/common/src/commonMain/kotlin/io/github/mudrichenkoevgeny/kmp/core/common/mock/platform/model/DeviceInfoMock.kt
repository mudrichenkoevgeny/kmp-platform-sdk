package io.github.mudrichenkoevgeny.kmp.core.common.mock.platform.model

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType

/**
 * Creates a deterministic mock [ClientDeviceInfo] for previews/tests.
 *
 * @param deviceId Device id used by networking headers.
 * @param deviceName Friendly device name.
 * @param clientType Client type to embed into the resulting [ClientDeviceInfo].
 * @param language Language code used as `Accept-Language`.
 * @param appVersion Application version string.
 * @param operationSystemVersion OS version string.
 */
@InternalApi
fun deviceInfoMock(
    deviceId: ClientDeviceId = ClientDeviceId.generate(),
    deviceName: String = "KMP Mock Device",
    clientType: ClientType = ClientType.ANDROID,
    language: String = "en",
    appVersion: String = "1.0.0-debug",
    operationSystemVersion: String = "16"
) = ClientDeviceInfo(
    clientType = clientType,
    deviceId = deviceId,
    deviceName = deviceName,
    language = language,
    appVersion = appVersion,
    operationSystemVersion = operationSystemVersion
)