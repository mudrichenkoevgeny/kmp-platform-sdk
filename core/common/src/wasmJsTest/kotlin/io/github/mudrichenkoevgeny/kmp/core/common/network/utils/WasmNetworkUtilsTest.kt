package io.github.mudrichenkoevgeny.kmp.core.common.network.utils

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WasmNetworkUtilsTest {

    @Test
    fun `failed to fetch message is no internet`() {
        assertTrue(isNoInternetException(Throwable("Failed to fetch")))
    }

    @Test
    fun `networkerror substring is no internet`() {
        assertTrue(isNoInternetException(Throwable("TypeError: NetworkError when attempting to fetch")))
    }

    @Test
    fun `dns substring is no internet`() {
        assertTrue(isNoInternetException(Throwable("ERR_NAME_NOT_RESOLVED dns probe failed")))
    }

    @Test
    fun `unrelated message is not no internet`() {
        assertFalse(isNoInternetException(Throwable("validation failed")))
    }

    @Test
    fun `null message is not no internet`() {
        assertFalse(isNoInternetException(Throwable()))
    }
}
