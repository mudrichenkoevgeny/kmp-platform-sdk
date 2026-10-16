package io.github.mudrichenkoevgeny.kmp.feature.user.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.DeepLinkParser
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.toUserIdentifierIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientUserNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.detail.IdentifierDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.list.SelfIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.detail.SessionDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.list.SelfSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.totp.main.TotpMainParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserApiPaths
import io.ktor.http.Url

class UserDeepLinkParser : DeepLinkParser<ClientUserNavigation> {
    override fun parse(url: Url): ClientUserNavigation? {
        val path = url.encodedPath.trimEnd('/').ifEmpty { "/" }
        val segments = path.split('/').filter { it.isNotEmpty() }

        if (segments.isEmpty() || "/${segments[0]}" != ClientUserNavigation.BASE_USER_ROUTE) {
            return null
        }

        val queryMap: Map<String, Any?> = url.parameters.entries().associate { (key, values) ->
            key to if (values.size == 1) values.first() else values
        }

        return when {
            path == ClientUserNavigation.BASE_USER_ROUTE || path == ClientUserNavigation.Main.route ->
                ClientUserNavigation.Main

            path == ClientUserNavigation.BASE_TOTP_ROUTE || path == ClientUserNavigation.TotpMain.ROUTE ->
                ClientUserNavigation.TotpMain(TotpMainParams.parse(queryMap))

            path == ClientUserNavigation.TotpRecoveryCodes.route ->
                ClientUserNavigation.TotpRecoveryCodes

            path == ClientUserNavigation.SessionList.ROUTE -> {
                val querySessionId = url.parameters[UserApiPaths.SESSION_ID]
                if (querySessionId != null) {
                    ClientUserNavigation.SessionDetail(SessionDetailParams.parse(queryMap))
                } else {
                    ClientUserNavigation.SessionList(SelfSessionListParams.parse(queryMap))
                }
            }

            segments.size >= 3 && "/${segments[0]}/${segments[1]}" == ClientUserNavigation.SessionList.ROUTE -> {
                val sessionId = segments[2].toUserSessionIdOrNull()
                if (sessionId != null) {
                    ClientUserNavigation.SessionDetail(SessionDetailParams(sessionId = sessionId.asHexDashString()))
                } else {
                    ClientUserNavigation.SessionList(SelfSessionListParams.parse(queryMap))
                }
            }

            path == ClientUserNavigation.IdentifierList.ROUTE -> {
                val queryIdentifierId = url.parameters[UserApiPaths.USER_IDENTIFIER_ID]
                if (queryIdentifierId != null) {
                    ClientUserNavigation.IdentifierDetail(IdentifierDetailParams.parse(queryMap))
                } else {
                    ClientUserNavigation.IdentifierList(SelfIdentifierListParams.parse(queryMap))
                }
            }

            segments.size >= 3 && "/${segments[0]}/${segments[1]}" == ClientUserNavigation.IdentifierList.ROUTE -> {
                val identifierId = segments[2].toUserIdentifierIdOrNull()
                if (identifierId != null) {
                    ClientUserNavigation.IdentifierDetail(IdentifierDetailParams(identifierId = identifierId.asHexDashString()))
                } else {
                    ClientUserNavigation.IdentifierList(SelfIdentifierListParams.parse(queryMap))
                }
            }

            else -> ClientUserNavigation.Main
        }
    }
}
