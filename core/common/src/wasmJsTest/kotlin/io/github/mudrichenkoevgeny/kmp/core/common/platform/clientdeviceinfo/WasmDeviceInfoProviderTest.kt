package io.github.mudrichenkoevgeny.kmp.core.common.platform.clientdeviceinfo

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
class WasmDeviceInfoProviderTest {

    @Test
    fun `getDeviceInfo uses web client type and app version`() {
        val provider = WasmClientDeviceInfoProvider(appVersion = "9.8.7")

        val info = provider.getClientDeviceInfo()

        assertEquals(ClientType.WEB, info.clientType)
        assertEquals("9.8.7", info.appVersion)
        assertEquals(WasmDeviceInfo.OS_VERSION, info.operationSystemVersion)
    }

    @Test
    fun `getDeviceInfo persists deviceId across calls`() {
        val provider = WasmClientDeviceInfoProvider(appVersion = "1.0.0")

        val firstInfo = provider.getClientDeviceInfo()
        val secondInfo = provider.getClientDeviceInfo()

        assertEquals(firstInfo.deviceId, secondInfo.deviceId)
    }
}
