package io.github.mudrichenkoevgeny.kmp.sampleclient.app.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.DeepLinkParser
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.navigation.deeplink.ClientUserDeepLinkParser
import io.github.mudrichenkoevgeny.kmp.feature.user.navigation.deeplink.UserDeepLinkParser
import io.ktor.http.Url

/**
 * Top-level deep link parser for the client sample application. Delegates URL parsing
 * to feature-specific parsers ([ClientUserDeepLinkParser] and [UserDeepLinkParser]).
 *
 * @param clientUserParser Parser handling authentication and authorization deep link paths.
 * @param userParser Parser handling user main and settings deep link paths.
 */
class ClientAppDeepLinkParser(
    private val clientUserParser: ClientUserDeepLinkParser = ClientUserDeepLinkParser(),
    private val userParser: UserDeepLinkParser = UserDeepLinkParser()
) : DeepLinkParser<ClientAppDeepLinkRoute> {

    /**
     * Parses the given [Url] into a top-level [ClientAppDeepLinkRoute], or returns `null` if unhandled.
     *
     * @param url Incoming application deep link URL.
     * @return Resolved [ClientAppDeepLinkRoute], or `null` if the URL pattern does not match any feature route.
     */
    override fun parse(url: Url): ClientAppDeepLinkRoute? {
        val clientLoginDestination = clientUserParser.parse(url)
        if (clientLoginDestination != null) {
            return ClientAppDeepLinkRoute.Login(clientLoginDestination)
        }

        val userDestination = userParser.parse(url)
        if (userDestination != null) {
            return ClientAppDeepLinkRoute.User(userDestination)
        }

        return null
    }
}
