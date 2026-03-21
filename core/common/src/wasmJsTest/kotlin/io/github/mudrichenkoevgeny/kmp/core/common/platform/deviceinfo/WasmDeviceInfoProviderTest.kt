package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo

import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import kotlin.test.Test
import kotlin.test.assertEquals

class WasmDeviceInfoProviderTest {

    @Test
    fun `getDeviceInfo uses web client type and app version`() {
        val provider = WasmDeviceInfoProvider(appVersion = "9.8.7")

        val info = provider.getDeviceInfo()

        assertEquals(UserClientType.WEB, info.clientType)
        assertEquals("9.8.7", info.appVersion)
        assertEquals(WasmDeviceInfo.OS_VERSION, info.osVersion)
    }
}
