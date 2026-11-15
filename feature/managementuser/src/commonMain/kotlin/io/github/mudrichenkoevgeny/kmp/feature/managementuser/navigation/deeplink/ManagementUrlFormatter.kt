package io.github.mudrichenkoevgeny.kmp.feature.managementuser.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.UrlFormatter
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.NavigationRoute
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.toFullUrl

/**
 * Formats navigation routes into full URL strings for management destinations.
 *
 * @param baseHost Base host for URL formatting.
 */
public class ManagementUrlFormatter(
    private val baseHost: String
) : UrlFormatter<NavigationRoute> {
    override fun format(destination: NavigationRoute): String = destination.toFullUrl(baseHost)
}
