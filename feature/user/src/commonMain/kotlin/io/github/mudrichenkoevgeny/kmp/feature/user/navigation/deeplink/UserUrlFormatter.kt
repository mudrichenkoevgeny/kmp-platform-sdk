package io.github.mudrichenkoevgeny.kmp.feature.user.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.UrlFormatter
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.toFullUrl
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientUserNavigation

class UserUrlFormatter(
    private val baseHost: String
) : UrlFormatter<ClientUserNavigation> {
    override fun format(destination: ClientUserNavigation): String {
        return destination.toFullUrl(baseHost)
    }
}
