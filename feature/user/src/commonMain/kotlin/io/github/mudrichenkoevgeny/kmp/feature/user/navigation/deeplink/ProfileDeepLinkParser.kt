package io.github.mudrichenkoevgeny.kmp.feature.user.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.DeepLinkParser
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.IdentifierListParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.toUserIdentifierIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrNull
import io.ktor.http.Url

class ProfileDeepLinkParser : DeepLinkParser<ProfileDestination> {
    override fun parse(url: Url): ProfileDestination? {
        val segments = url.segments.filter { it.isNotEmpty() }
        if (segments.isEmpty()) return null

        val rootSegment = segments[0]
        val p1 = segments.getOrNull(1)
        val p2 = segments.getOrNull(2)

        if (rootSegment != ProfileDeepLinkConstants.Segment.ROOT) return null

        return when {
            p1 == null -> ProfileDestination.Main
            p1 == ProfileDeepLinkConstants.Segment.TOTP && p2 == null -> {
                val token = url.parameters[ProfileDeepLinkConstants.Query.PARAM_TOKEN]
                ProfileDestination.TotpMain(TotpMainParams(token = token))
            }
            p1 == ProfileDeepLinkConstants.Segment.TOTP && p2 == ProfileDeepLinkConstants.Segment.SETUP -> {
                val token = url.parameters[ProfileDeepLinkConstants.Query.PARAM_TOKEN]
                ProfileDestination.TotpMain(TotpMainParams(token = token))
            }
            p1 == ProfileDeepLinkConstants.Segment.TOTP && p2 == ProfileDeepLinkConstants.Segment.RECOVERY -> ProfileDestination.TotpRecoveryCodes
            p1 == ProfileDeepLinkConstants.Segment.SESSIONS && p2 == null -> ProfileDestination.Sessions
            p1 == ProfileDeepLinkConstants.Segment.SESSIONS && p2 != null -> {
                val sessionId = p2.toUserSessionIdOrNull() ?: return ProfileDestination.Sessions
                ProfileDestination.SessionDetail(SessionDetailParams(sessionId.asHexDashString()))
            }
            p1 == ProfileDeepLinkConstants.Segment.IDENTIFIERS && p2 == null -> {
                val dialog = url.parameters[ProfileDeepLinkConstants.Query.PARAM_DIALOG]
                val step = url.parameters[ProfileDeepLinkConstants.Query.PARAM_STEP]
                val value = url.parameters[ProfileDeepLinkConstants.Query.PARAM_EMAIL]
                    ?: url.parameters[ProfileDeepLinkConstants.Query.PARAM_PHONE]
                ProfileDestination.Identifiers(IdentifierListParams(dialog = dialog, step = step, value = value))
            }
            p1 == ProfileDeepLinkConstants.Segment.IDENTIFIERS && p2 != null -> {
                val identifierId = p2.toUserIdentifierIdOrNull()
                if (identifierId != null) {
                    ProfileDestination.IdentifierDetail(IdentifierDetailParams(identifierId.asHexDashString()))
                } else {
                    val dialog = url.parameters[ProfileDeepLinkConstants.Query.PARAM_DIALOG]
                    val step = url.parameters[ProfileDeepLinkConstants.Query.PARAM_STEP]
                    val value = url.parameters[ProfileDeepLinkConstants.Query.PARAM_EMAIL]
                        ?: url.parameters[ProfileDeepLinkConstants.Query.PARAM_PHONE]
                    ProfileDestination.Identifiers(IdentifierListParams(dialog = dialog, step = step, value = value))
                }
            }
            else -> ProfileDestination.Main
        }
    }
}
