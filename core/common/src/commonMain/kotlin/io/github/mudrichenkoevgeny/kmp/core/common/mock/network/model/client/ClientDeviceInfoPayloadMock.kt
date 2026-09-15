package io.github.mudrichenkoevgeny.kmp.core.common.mock.network.model.client

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.client.ClientDeviceInfoPayload
import kotlin.uuid.Uuid

@InternalApi
fun clientDeviceInfoPayloadMock(
    clientType: String? = ClientType.ANDROID.serialName,
    language: String? = "en",
    deviceId: String? = Uuid.random().toHexDashString(),
    deviceName: String? = "Pixel",
    appVersion: String? = "1.0",
    operationSystemVersion: String? = "14"
) = ClientDeviceInfoPayload(
    clientType = clientType,
    language = language,
    deviceId = deviceId,
    deviceName = deviceName,
    appVersion = appVersion,
    operationSystemVersion = operationSystemVersion
)
