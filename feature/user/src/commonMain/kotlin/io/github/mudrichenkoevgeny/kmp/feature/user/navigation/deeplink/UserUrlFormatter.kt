package io.github.mudrichenkoevgeny.kmp.feature.user.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.UrlFormatter
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.toFullUrl
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientUserNavigation

/**
 * Formats [ClientUserNavigation] destinations into full URL strings.
 *
 * @param baseHost Base host string for deep link routing.
 */
public class UserUrlFormatter(
    private val baseHost: String
) : UrlFormatter<ClientUserNavigation> {
    public override fun format(destination: ClientUserNavigation): String = destination.toFullUrl(baseHost)
}
