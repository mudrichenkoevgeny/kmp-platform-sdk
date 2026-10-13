package io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink

import io.ktor.http.Url

/**
 * Interface for parsing deep links.
 * 
 * @param Route The type of the route that the parser returns.
 */
interface DeepLinkParser<out Route : Any> {
    /**
     * Parses the given URL and returns the corresponding route, or null if it cannot be parsed.
     */
    fun parse(url: Url): Route?
}
