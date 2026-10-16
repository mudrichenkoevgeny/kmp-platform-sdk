package io.github.mudrichenkoevgeny.kmp.feature.managementuser.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.UrlFormatter
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.NavigationRoute
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.toFullUrl

class ManagementUrlFormatter(
    private val baseHost: String
) : UrlFormatter<NavigationRoute> {
    override fun format(destination: NavigationRoute): String {
        return destination.toFullUrl(baseHost)
    }
}
