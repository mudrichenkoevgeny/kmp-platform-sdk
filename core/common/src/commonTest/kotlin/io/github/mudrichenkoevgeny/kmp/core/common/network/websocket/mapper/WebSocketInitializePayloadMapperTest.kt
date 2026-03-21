package io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.mapper

import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import kotlin.test.Test
import kotlin.test.assertEquals

class WebSocketInitializePayloadMapperTest {

    @Test
    fun `maps DeviceInfo into websocket initialization payload`() {
        val deviceId = DeviceId.generate()
        val deviceInfo = DeviceInfo(
            clientType = UserClientType.WEB,
            deviceId = deviceId,
            deviceName = "My Device",
            language = "en",
            appVersion = "1.2.3",
            osVersion = "16"
        )

        val payload = deviceInfo.toWebSocketInitializePayload()

        assertEquals(UserClientType.WEB.serialName, payload.clientType)
        assertEquals(deviceInfo.deviceId.asHexDashString(), payload.deviceId)
        assertEquals(deviceInfo.deviceName, payload.deviceName)
        assertEquals(deviceInfo.language, payload.language)
        assertEquals(deviceInfo.appVersion, payload.appVersion)
        assertEquals(deviceInfo.osVersion, payload.operationSystemVersion)
    }
}

