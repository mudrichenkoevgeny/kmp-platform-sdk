package io.github.mudrichenkoevgeny.kmp.feature.managementuser.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.DeepLinkParser
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.ManagementDestination
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail.AuditEventDetailParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list.AuditEventListParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist.UserSessionListParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.toAuditEventIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.toUserIdentifierIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.toUserIdOrNull
import io.ktor.http.Url

class ManagementDeepLinkParser : DeepLinkParser<ManagementDestination> {
    override fun parse(url: Url): ManagementDestination? {
        val segments = url.segments.filter { it.isNotEmpty() }
        if (segments.isEmpty() || segments[0] != ManagementDeepLinkConstants.Segment.ROOT) return null

        val p1 = segments.getOrNull(1)
        val p2 = segments.getOrNull(2)
        val p3 = segments.getOrNull(3)

        return when {
            p1 == null -> ManagementDestination.Main
            p1 == ManagementDeepLinkConstants.Segment.SETTINGS && p2 == ManagementDeepLinkConstants.Segment.AUTH -> {
                val dialog = url.parameters[ManagementDeepLinkConstants.Query.PARAM_DIALOG]
                ManagementDestination.EditAuthSettings(SettingsParams(dialog))
            }
            p1 == ManagementDeepLinkConstants.Segment.SETTINGS && p2 == ManagementDeepLinkConstants.Segment.GLOBAL -> {
                val dialog = url.parameters[ManagementDeepLinkConstants.Query.PARAM_DIALOG]
                ManagementDestination.EditGlobalSettings(SettingsParams(dialog))
            }
            p1 == ManagementDeepLinkConstants.Segment.SETTINGS && p2 == ManagementDeepLinkConstants.Segment.SECURITY -> {
                val dialog = url.parameters[ManagementDeepLinkConstants.Query.PARAM_DIALOG]
                ManagementDestination.EditSecuritySettings(SettingsParams(dialog))
            }
            p1 == ManagementDeepLinkConstants.Segment.USERS && p2 == null -> ManagementDestination.GlobalUserList
            p1 == ManagementDeepLinkConstants.Segment.USERS && p2 == ManagementDeepLinkConstants.Segment.CREATE -> ManagementDestination.CreateUser
            p1 == ManagementDeepLinkConstants.Segment.USERS && p2 != null -> {
                val userId = p2.toUserIdOrNull() ?: return ManagementDestination.GlobalUserList
                when (p3) {
                    null -> ManagementDestination.UserDetail(UserDetailParams(userId.asHexDashString()))
                    ManagementDeepLinkConstants.Segment.SESSIONS -> ManagementDestination.UserSessionList(UserSessionListParams(userId.asHexDashString()))
                    ManagementDeepLinkConstants.Segment.IDENTIFIERS -> ManagementDestination.UserIdentifierList(UserIdentifierListParams(userId.asHexDashString()))
                    else -> ManagementDestination.UserDetail(UserDetailParams(userId.asHexDashString()))
                }
            }
            p1 == ManagementDeepLinkConstants.Segment.AUDIT && p2 == null -> {
                val actorIds = url.parameters.getAll(ManagementDeepLinkConstants.Query.PARAM_ACTOR_IDS)
                val filters = if (!actorIds.isNullOrEmpty()) AuditEventListParams.Filters(actorIds) else null
                ManagementDestination.AuditEventList(AuditEventListParams(filters))
            }
            p1 == ManagementDeepLinkConstants.Segment.AUDIT && p2 != null -> {
                val eventId = p2.toAuditEventIdOrNull() ?: return ManagementDestination.AuditEventList()
                ManagementDestination.AuditEventDetail(AuditEventDetailParams(eventId.asHexDashString()))
            }
            p1 == ManagementDeepLinkConstants.Segment.SESSIONS && p2 == null -> ManagementDestination.GlobalSessionList
            p1 == ManagementDeepLinkConstants.Segment.SESSIONS && p2 != null -> {
                val sessionId = p2.toUserSessionIdOrNull() ?: return ManagementDestination.GlobalSessionList
                ManagementDestination.SessionDetail(SessionDetailParams(sessionId.asHexDashString()))
            }
            p1 == ManagementDeepLinkConstants.Segment.IDENTIFIERS && p2 == null -> ManagementDestination.GlobalIdentifierList
            p1 == ManagementDeepLinkConstants.Segment.IDENTIFIERS && p2 != null -> {
                val identifierId = p2.toUserIdentifierIdOrNull() ?: return ManagementDestination.GlobalIdentifierList
                ManagementDestination.IdentifierDetail(IdentifierDetailParams(identifierId.asHexDashString()))
            }
            else -> ManagementDestination.Main
        }
    }
}
