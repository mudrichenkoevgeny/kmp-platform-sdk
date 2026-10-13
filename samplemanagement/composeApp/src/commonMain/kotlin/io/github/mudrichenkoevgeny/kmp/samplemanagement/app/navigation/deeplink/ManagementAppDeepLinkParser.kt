package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.DeepLinkParser
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.navigation.deeplink.ManagementDeepLinkParser
import io.github.mudrichenkoevgeny.kmp.feature.user.navigation.deeplink.ProfileDeepLinkParser
import io.ktor.http.Url

class ManagementAppDeepLinkParser(
    private val managementParser: ManagementDeepLinkParser = ManagementDeepLinkParser(),
    private val profileParser: ProfileDeepLinkParser = ProfileDeepLinkParser()
) : DeepLinkParser<ManagementAppDeepLinkRoute> {
    override fun parse(url: Url): ManagementAppDeepLinkRoute? {
        val managementDestination = managementParser.parse(url)
        if (managementDestination != null) {
            return ManagementAppDeepLinkRoute.Management(managementDestination)
        }
        
        val profileDestination = profileParser.parse(url)
        if (profileDestination != null) {
            return ManagementAppDeepLinkRoute.Profile(profileDestination)
        }
        
        return null
    }
}
