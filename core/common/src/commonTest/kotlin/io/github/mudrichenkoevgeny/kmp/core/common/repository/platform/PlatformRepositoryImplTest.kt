package io.github.mudrichenkoevgeny.kmp.core.common.repository.platform

import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceId
import io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model.DeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import kotlin.uuid.Uuid
import kotlin.test.Test
import kotlin.test.assertEquals

class PlatformRepositoryImplTest {

    @Test
    fun `returns provided device info`() {
        val uuid = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val deviceId = DeviceId(uuid)

        val deviceInfo = DeviceInfo(
            clientType = UserClientType.ANDROID,
            deviceId = deviceId,
            deviceName = "Test Device",
            language = "en",
            appVersion = "1.0.0",
            osVersion = "16"
        )

        val repo = PlatformRepositoryImpl(deviceInfo)

        assertEquals(deviceInfo, repo.getDeviceInfo())
    }
}

