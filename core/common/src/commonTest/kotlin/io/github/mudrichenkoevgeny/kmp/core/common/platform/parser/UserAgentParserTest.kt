package io.github.mudrichenkoevgeny.kmp.core.common.platform.parser

import kotlin.test.Test
import kotlin.test.assertEquals

class UserAgentParserTest {

    @Test
    fun getBrowser_returnsChrome_whenStandardChromeUserAgent() {
        val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36"

        val result = UserAgentParser.getBrowser(userAgent)

        assertEquals("Chrome", result)
    }

    @Test
    fun getBrowser_returnsEdge_whenEdgeUserAgentWithChromeAndSafariTokens() {
        val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36 Edg/128.0.0.0"

        val result = UserAgentParser.getBrowser(userAgent)

        assertEquals("Edge", result)
    }

    @Test
    fun getBrowser_returnsOpera_whenOperaOprTokenPresent() {
        val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36 OPR/114.0.0.0"

        val result = UserAgentParser.getBrowser(userAgent)

        assertEquals("Opera", result)
    }

    @Test
    fun getBrowser_returnsVivaldi_whenVivaldiTokenPresent() {
        val userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36 Vivaldi/6.9.3447.48"

        val result = UserAgentParser.getBrowser(userAgent)

        assertEquals("Vivaldi", result)
    }

    @Test
    fun getBrowser_returnsYandexBrowser_whenYandexTokenPresent() {
        val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 YaBrowser/24.7.0.0 Safari/537.36"

        val result = UserAgentParser.getBrowser(userAgent)

        assertEquals("Yandex Browser", result)
    }

    @Test
    fun getBrowser_returnsFirefox_whenFirefoxUserAgent() {
        val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:130.0) Gecko/20100101 Firefox/130.0"

        val result = UserAgentParser.getBrowser(userAgent)

        assertEquals("Firefox", result)
    }

    @Test
    fun getBrowser_returnsSafari_whenSafariUserAgentWithoutChromeToken() {
        val userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 14_6_1) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Safari/605.1.15"

        val result = UserAgentParser.getBrowser(userAgent)

        assertEquals("Safari", result)
    }

    @Test
    fun getBrowser_returnsUnknown_whenUnrecognizedUserAgent() {
        val userAgent = "CustomApp/1.0.0 (UnknownDevice)"

        val result = UserAgentParser.getBrowser(userAgent)

        assertEquals("Web Browser", result)
    }

    @Test
    fun getOs_returnsWindows10Or11_whenWindowsNt10TokenPresent() {
        val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36"

        val result = UserAgentParser.getOs(userAgent)

        assertEquals("Windows 10/11", result)
    }

    @Test
    fun getOs_returnsWindows_whenLegacyWindowsNtTokenPresent() {
        val userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36"

        val result = UserAgentParser.getOs(userAgent)

        assertEquals("Windows", result)
    }

    @Test
    fun getOs_returnsMacOs_whenMacintoshTokenPresent() {
        val userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Safari/605.1.15"

        val result = UserAgentParser.getOs(userAgent)

        assertEquals("macOS", result)
    }

    @Test
    fun getOs_returnsIos_whenIPhoneTokenPresent() {
        val userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 17_6_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.5 Mobile/15E148 Safari/604.1"

        val result = UserAgentParser.getOs(userAgent)

        assertEquals("iOS", result)
    }

    @Test
    fun getOs_returnsAndroid_whenAndroidTokenPresent() {
        val userAgent = "Mozilla/5.0 (Linux; Android 14; SM-S928B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.6613.88 Mobile Safari/537.36"

        val result = UserAgentParser.getOs(userAgent)

        assertEquals("Android", result)
    }

    @Test
    fun getOs_returnsLinux_whenPureLinuxUserAgent() {
        val userAgent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:130.0) Gecko/20100101 Firefox/130.0"

        val result = UserAgentParser.getOs(userAgent)

        assertEquals("Linux", result)
    }

    @Test
    fun getOs_returnsChromeOs_whenCrOsTokenPresent() {
        val userAgent = "Mozilla/5.0 (X11; CrOS x86_64 14541.0.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36"

        val result = UserAgentParser.getOs(userAgent)

        assertEquals("ChromeOS", result)
    }

    @Test
    fun getOs_returnsUnknown_whenUnrecognizedUserAgent() {
        val userAgent = "CustomApp/1.0.0"

        val result = UserAgentParser.getOs(userAgent)

        assertEquals("Web", result)
    }

    @Test
    fun getDeviceName_formatsBrowserAndOsProperly() {
        val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36"

        val result = UserAgentParser.getDeviceName(userAgent)

        assertEquals("Chrome on Windows 10/11", result)
    }
}