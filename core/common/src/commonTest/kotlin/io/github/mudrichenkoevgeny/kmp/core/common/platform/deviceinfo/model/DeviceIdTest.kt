package io.github.mudrichenkoevgeny.kmp.core.common.platform.deviceinfo.model

import kotlin.uuid.Uuid
import kotlin.test.Test
import kotlin.test.assertEquals

class DeviceIdTest {

    @Test
    fun `asHexDashString matches underlying Uuid`() {
        val uuid = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val deviceId = DeviceId(uuid)

        assertEquals(uuid.toHexDashString(), deviceId.asHexDashString())
    }
}

