package io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.client

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType

@InternalApi
fun clientDeviceInfoMock(
    clientType: ClientType? = ClientType.ANDROID,
    language: String? = "en",
    deviceId: ClientDeviceId? = ClientDeviceId.generate(),
    deviceName: String? = "Pixel",
    appVersion: String? = "1.0",
    operationSystemVersion: String? = "14"
) = ClientDeviceInfo(
    clientType = clientType,
    language = language,
    deviceId = deviceId,
    deviceName = deviceName,
    appVersion = appVersion,
    operationSystemVersion = operationSystemVersion
)
