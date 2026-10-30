package io.github.mudrichenkoevgeny.kmp.feature.clientuser.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.UrlFormatter
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.toFullUrl
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientAuthNavigation

class ClientUrlFormatter(
    private val baseHost: String
) : UrlFormatter<ClientAuthNavigation> {
    override fun format(destination: ClientAuthNavigation): String {
        return destination.toFullUrl(baseHost)
    }
}
