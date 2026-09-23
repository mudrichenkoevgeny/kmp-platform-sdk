package io.github.mudrichenkoevgeny.kmp.core.common.platform.parser

/**
 * Utility object for parsing browser and operating system details from a user-agent header string.
 */
object UserAgentParser {

    /**
     * Composes a human-readable device name in the format "$browser on $operatingSystem".
     *
     * @param userAgent Raw user-agent string.
     * @return Formatted device representation.
     */
    fun getDeviceName(userAgent: String): String {
        val browser = getBrowser(userAgent)
        val operationSystem = getOs(userAgent)
        return "$browser on $operationSystem"
    }

    /**
     * Determines the web browser name based on signature tokens found within the user-agent string.
     *
     * @param userAgent Raw user-agent string.
     * @return Identified browser name or [BROWSER_NAME_UNKNOWN] if none matched.
     */
    fun getBrowser(userAgent: String): String {
        return when {
            userAgent.contains(BROWSER_TOKEN_EDGE) -> BROWSER_NAME_EDGE
            userAgent.contains(BROWSER_TOKEN_OPERA_OPR) || userAgent.contains(BROWSER_TOKEN_OPERA) -> BROWSER_NAME_OPERA
            userAgent.contains(BROWSER_TOKEN_VIVALDI) -> BROWSER_NAME_VIVALDI
            userAgent.contains(BROWSER_TOKEN_YANDEX) -> BROWSER_NAME_YANDEX
            userAgent.contains(BROWSER_TOKEN_CHROME) -> BROWSER_NAME_CHROME
            userAgent.contains(BROWSER_TOKEN_FIREFOX) -> BROWSER_NAME_FIREFOX
            userAgent.contains(BROWSER_TOKEN_SAFARI) && !userAgent.contains(BROWSER_TOKEN_CHROME) -> BROWSER_NAME_SAFARI
            else -> BROWSER_NAME_UNKNOWN
        }
    }

    /**
     * Determines the operating system name based on signature tokens found within the user-agent string.
     *
     * @param userAgent Raw user-agent string.
     * @return Identified operating system name or [OS_NAME_UNKNOWN] if none matched.
     */
    fun getOs(userAgent: String): String {
        return when {
            userAgent.contains(OS_TOKEN_WINDOWS_10_OR_11) -> OS_NAME_WINDOWS_10_OR_11
            userAgent.contains(OS_TOKEN_WINDOWS_NT) -> OS_NAME_WINDOWS
            userAgent.contains(OS_TOKEN_IPHONE) || userAgent.contains(OS_TOKEN_IPAD) || userAgent.contains(OS_TOKEN_IPOD) -> OS_NAME_IOS
            userAgent.contains(OS_TOKEN_MACINTOSH) || userAgent.contains(OS_TOKEN_MAC_OS_X) -> OS_NAME_MACOS
            userAgent.contains(OS_TOKEN_ANDROID) -> OS_NAME_ANDROID
            userAgent.contains(OS_TOKEN_LINUX) -> OS_NAME_LINUX
            userAgent.contains(OS_TOKEN_CHROME_OS) -> OS_NAME_CHROME_OS
            else -> OS_NAME_UNKNOWN
        }
    }

    private const val BROWSER_TOKEN_EDGE = "Edg/"
    private const val BROWSER_TOKEN_OPERA_OPR = "OPR/"
    private const val BROWSER_TOKEN_OPERA = "Opera"
    private const val BROWSER_TOKEN_VIVALDI = "Vivaldi/"
    private const val BROWSER_TOKEN_YANDEX = "YaBrowser/"
    private const val BROWSER_TOKEN_CHROME = "Chrome/"
    private const val BROWSER_TOKEN_FIREFOX = "Firefox/"
    private const val BROWSER_TOKEN_SAFARI = "Safari/"

    private const val BROWSER_NAME_EDGE = "Edge"
    private const val BROWSER_NAME_OPERA = "Opera"
    private const val BROWSER_NAME_VIVALDI = "Vivaldi"
    private const val BROWSER_NAME_YANDEX = "Yandex Browser"
    private const val BROWSER_NAME_CHROME = "Chrome"
    private const val BROWSER_NAME_FIREFOX = "Firefox"
    private const val BROWSER_NAME_SAFARI = "Safari"
    private const val BROWSER_NAME_UNKNOWN = "Web Browser"

    private const val OS_TOKEN_WINDOWS_10_OR_11 = "Windows NT 10.0"
    private const val OS_TOKEN_WINDOWS_NT = "Windows NT"
    private const val OS_TOKEN_MACINTOSH = "Macintosh"
    private const val OS_TOKEN_MAC_OS_X = "Mac OS X"
    private const val OS_TOKEN_IPHONE = "iPhone"
    private const val OS_TOKEN_IPAD = "iPad"
    private const val OS_TOKEN_IPOD = "iPod"
    private const val OS_TOKEN_ANDROID = "Android"
    private const val OS_TOKEN_LINUX = "Linux"
    private const val OS_TOKEN_CHROME_OS = "CrOS"

    private const val OS_NAME_WINDOWS_10_OR_11 = "Windows 10/11"
    private const val OS_NAME_WINDOWS = "Windows"
    private const val OS_NAME_MACOS = "macOS"
    private const val OS_NAME_IOS = "iOS"
    private const val OS_NAME_ANDROID = "Android"
    private const val OS_NAME_LINUX = "Linux"
    private const val OS_NAME_CHROME_OS = "ChromeOS"
    private const val OS_NAME_UNKNOWN = "Web"
}