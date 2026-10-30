package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.DeepLinkParser
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.navigation.deeplink.ManagementUserDeepLinkParser
import io.github.mudrichenkoevgeny.kmp.feature.user.navigation.deeplink.UserDeepLinkParser
import io.ktor.http.Url

/**
 * Top-level deep link parser for the management sample portal application. Delegates URL parsing
 * to feature-specific parsers ([ManagementUserDeepLinkParser] and [UserDeepLinkParser]).
 *
 * @param managementParser Parser handling administrative management portal deep link paths.
 * @param userParser Parser handling staff user main and settings deep link paths.
 */
class ManagementAppDeepLinkParser(
    private val managementParser: ManagementUserDeepLinkParser = ManagementUserDeepLinkParser(),
    private val userParser: UserDeepLinkParser = UserDeepLinkParser()
) : DeepLinkParser<ManagementAppDeepLinkRoute> {

    /**
     * Parses the given [Url] into a top-level [ManagementAppDeepLinkRoute], or returns `null` if unhandled.
     *
     * @param url Incoming application deep link URL.
     * @return Resolved [ManagementAppDeepLinkRoute], or `null` if the URL pattern does not match any feature route.
     */
    override fun parse(url: Url): ManagementAppDeepLinkRoute? {
        val managementDestination = managementParser.parse(url)
        if (managementDestination != null) {
            return ManagementAppDeepLinkRoute.Management(managementDestination)
        }

        val userDestination = userParser.parse(url)
        if (userDestination != null) {
            return ManagementAppDeepLinkRoute.User(userDestination)
        }

        return null
    }
}
