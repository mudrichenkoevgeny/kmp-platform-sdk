package io.github.mudrichenkoevgeny.kmp.core.common.network.utils

import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AndroidNetworkUtilsTest {

    @Test
    fun `unknown host is no internet`() {
        assertTrue(isNoInternetException(UnknownHostException()))
    }

    @Test
    fun `connect exception is no internet`() {
        assertTrue(isNoInternetException(ConnectException()))
    }

    @Test
    fun `socket timeout is no internet`() {
        assertTrue(isNoInternetException(SocketTimeoutException()))
    }

    @Test
    fun `io exception with unable to resolve host message is no internet`() {
        assertTrue(isNoInternetException(IOException("Unable to resolve host \"example.com\"")))
    }

    @Test
    fun `io exception with connection refused message is no internet`() {
        assertTrue(isNoInternetException(IOException("Connection refused")))
    }

    @Test
    fun `generic io exception without markers is not no internet`() {
        assertFalse(isNoInternetException(IOException("Something else")))
    }

    @Test
    fun `non io throwable is not no internet`() {
        assertFalse(isNoInternetException(RuntimeException("network")))
    }
}
