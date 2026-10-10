package io.github.mudrichenkoevgeny.kmp.core.common.platform.clientdeviceinfo.model

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@InternalApi
class ClientDeviceInfoTest {

    @Test
    fun `isMobileClient is true for Android`() {
        val clientDeviceInfo = ClientDeviceInfo(
            deviceId = ClientDeviceId.generate(),
            deviceName = "Test",
            clientType = ClientType.ANDROID,
            language = "en",
            appVersion = "1.0.0",
            operationSystemVersion = "16"
        )

        assertTrue(clientDeviceInfo.isMobileClient())
    }

    @Test
    fun `isMobileClient is true for iOS`() {
        val clientDeviceInfo = ClientDeviceInfo(
            deviceId = ClientDeviceId.generate(),
            deviceName = "Test",
            clientType = ClientType.IOS,
            language = "en",
            appVersion = "1.0.0",
            operationSystemVersion = "16"
        )

        assertTrue(clientDeviceInfo.isMobileClient())
    }

    @Test
    fun `isMobileClient is false for Web`() {
        val clientDeviceInfo = ClientDeviceInfo(
            deviceId = ClientDeviceId.generate(),
            deviceName = "Test",
            clientType = ClientType.WEB,
            language = "en",
            appVersion = "1.0.0",
            operationSystemVersion = "16"
        )

        assertFalse(clientDeviceInfo.isMobileClient())
    }
}

