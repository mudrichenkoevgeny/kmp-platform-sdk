package io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink

/**
 * Interface for formatting destinations into deep link URL strings.
 */
interface UrlFormatter<in T : Any> {
    /**
     * Formats the given destination into a URL string.
     */
    fun format(destination: T): String
}
