package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model

import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeviceInfoTest {

    @Test
    fun `isMobileClient is true for Android`() {
        val deviceInfo = DeviceInfo(
            clientType = UserClientType.ANDROID,
            deviceId = DeviceId.generate(),
            deviceName = "Test",
            language = "en",
            appVersion = "1.0.0",
            osVersion = "16"
        )

        assertTrue(deviceInfo.isMobileClient())
    }

    @Test
    fun `isMobileClient is true for iOS`() {
        val deviceInfo = DeviceInfo(
            clientType = UserClientType.IOS,
            deviceId = DeviceId.generate(),
            deviceName = "Test",
            language = "en",
            appVersion = "1.0.0",
            osVersion = "16"
        )

        assertTrue(deviceInfo.isMobileClient())
    }

    @Test
    fun `isMobileClient is false for Web`() {
        val deviceInfo = DeviceInfo(
            clientType = UserClientType.WEB,
            deviceId = DeviceId.generate(),
            deviceName = "Test",
            language = "en",
            appVersion = "1.0.0",
            osVersion = "16"
        )

        assertFalse(deviceInfo.isMobileClient())
    }
}

