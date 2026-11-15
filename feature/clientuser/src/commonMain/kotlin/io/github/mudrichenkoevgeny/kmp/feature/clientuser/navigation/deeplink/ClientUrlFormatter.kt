package io.github.mudrichenkoevgeny.kmp.feature.clientuser.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.UrlFormatter
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.toFullUrl
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientAuthNavigation

/**
 * Formats [ClientAuthNavigation] routes into full URLs using the specified base host.
 *
 * @param baseHost Base host string used for constructing complete URLs.
 */
public class ClientUrlFormatter(
    private val baseHost: String
) : UrlFormatter<ClientAuthNavigation> {
    override fun format(destination: ClientAuthNavigation): String = destination.toFullUrl(baseHost)
}
