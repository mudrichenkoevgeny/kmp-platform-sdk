package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
class WasmDeviceInfoProviderTest {

    @Test
    fun `getDeviceInfo uses web client type and app version`() {
        val provider = WasmDeviceInfoProvider(appVersion = "9.8.7")

        val info = provider.getDeviceInfo()

        assertEquals(ClientType.WEB, info.clientType)
        assertEquals("9.8.7", info.appVersion)
        assertEquals(WasmDeviceInfo.OS_VERSION, info.operationSystemVersion)
    }

    @Test
    fun `getDeviceInfo persists deviceId across calls`() {
        val provider = WasmDeviceInfoProvider(appVersion = "1.0.0")

        val firstInfo = provider.getDeviceInfo()
        val secondInfo = provider.getDeviceInfo()

        assertEquals(firstInfo.deviceId, secondInfo.deviceId)
    }
}
