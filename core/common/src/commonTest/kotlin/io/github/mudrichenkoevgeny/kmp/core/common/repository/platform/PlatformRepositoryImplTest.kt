package io.github.mudrichenkoevgeny.kmp.core.common.repository.platform

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientDeviceInfo
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import kotlin.uuid.Uuid
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
class PlatformRepositoryImplTest {

    @Test
    fun `returns provided device info`() {
        val uuid = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val deviceId = ClientDeviceId(uuid)

        val deviceInfo = ClientDeviceInfo(
            deviceId = deviceId,
            deviceName = "Test Device",
            clientType = ClientType.ANDROID,
            language = "en",
            appVersion = "1.0.0",
            operationSystemVersion = "16"
        )

        val repo = PlatformRepositoryImpl(deviceInfo)

        assertEquals(deviceInfo, repo.getDeviceInfo())
    }
}

