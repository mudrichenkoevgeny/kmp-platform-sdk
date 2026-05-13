package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@InternalApi
class DeviceInfoTest {

    @Test
    fun `isMobileClient is true for Android`() {
        val deviceInfo = ClientDeviceInfo(
            deviceId = ClientDeviceId.generate(),
            deviceName = "Test",
            clientType = ClientType.ANDROID,
            language = "en",
            appVersion = "1.0.0",
            operationSystemVersion = "16"
        )

        assertTrue(deviceInfo.isMobileClient())
    }

    @Test
    fun `isMobileClient is true for iOS`() {
        val deviceInfo = ClientDeviceInfo(
            deviceId = ClientDeviceId.generate(),
            deviceName = "Test",
            clientType = ClientType.IOS,
            language = "en",
            appVersion = "1.0.0",
            operationSystemVersion = "16"
        )

        assertTrue(deviceInfo.isMobileClient())
    }

    @Test
    fun `isMobileClient is false for Web`() {
        val deviceInfo = ClientDeviceInfo(
            deviceId = ClientDeviceId.generate(),
            deviceName = "Test",
            clientType = ClientType.WEB,
            language = "en",
            appVersion = "1.0.0",
            operationSystemVersion = "16"
        )

        assertFalse(deviceInfo.isMobileClient())
    }
}

