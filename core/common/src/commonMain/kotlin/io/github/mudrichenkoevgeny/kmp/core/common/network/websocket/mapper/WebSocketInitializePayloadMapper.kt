package io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.mapper

import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.network.model.websocket.payload.WebSocketInitializePayload

/**
 * Maps SDK [DeviceInfo] into a websocket initialization payload expected by the backend contract.
 */
fun DeviceInfo.toWebSocketInitializePayload() = WebSocketInitializePayload(
    clientType = clientType.serialName,
    deviceId = deviceId.asHexDashString(),
    deviceName = deviceName,
    language = language,
    appVersion = appVersion,
    operationSystemVersion = osVersion
)