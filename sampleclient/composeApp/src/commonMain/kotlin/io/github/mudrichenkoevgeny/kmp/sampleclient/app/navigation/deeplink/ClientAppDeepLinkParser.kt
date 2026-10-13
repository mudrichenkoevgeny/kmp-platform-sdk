package io.github.mudrichenkoevgeny.kmp.sampleclient.app.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.DeepLinkParser
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.navigation.deeplink.ClientUserDeepLinkParser
import io.github.mudrichenkoevgeny.kmp.feature.user.navigation.deeplink.ProfileDeepLinkParser
import io.ktor.http.Url

class ClientAppDeepLinkParser(
    private val clientUserParser: ClientUserDeepLinkParser = ClientUserDeepLinkParser(),
    private val profileParser: ProfileDeepLinkParser = ProfileDeepLinkParser()
) : DeepLinkParser<ClientAppDeepLinkRoute> {
    override fun parse(url: Url): ClientAppDeepLinkRoute? {
        val clientLoginDestination = clientUserParser.parse(url)
        if (clientLoginDestination != null) {
            return ClientAppDeepLinkRoute.Login(clientLoginDestination)
        }

        val profileDestination = profileParser.parse(url)
        if (profileDestination != null) {
            return ClientAppDeepLinkRoute.Profile(profileDestination)
        }

        return null
    }
}
