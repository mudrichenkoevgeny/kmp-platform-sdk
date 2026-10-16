package io.github.mudrichenkoevgeny.kmp.feature.managementuser.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.DeepLinkParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.toAuditEventIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.route.management.ManagementAuditNavigation
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.screen.management.audit.detail.ManagementAuditEventDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.screen.management.audit.list.ManagementAuditEventListParams
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.CommonNavigationRoutes
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.NavigationRoute
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.management.ManagementMainNavigation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.navigation.route.management.ManagementSecurityNavigation
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.navigation.route.management.ManagementSettingsNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.toUserIdentifierIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.toUserIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.management.ManagementAuthSettingsNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.management.ManagementUsersNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.detail.IdentifierDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.detail.SessionDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.globallist.ManagementGlobalIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.identifier.userlist.ManagementUserIdentifierListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.globallist.ManagementGlobalSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.session.userlist.ManagementUserSessionListParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.user.detail.UserDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.management.user.globallist.ManagementUserListParams
import io.ktor.http.Url

class ManagementDeepLinkParser : DeepLinkParser<NavigationRoute> {
    override fun parse(url: Url): NavigationRoute? {
        val queryMap: Map<String, Any?> = url.parameters.entries().associate { (key, values) ->
            key to if (values.size == 1) values.first() else values
        }

        val path = url.encodedPath.removePrefix(MANAGEMENT_PREFIX).trimEnd('/').ifEmpty { "/" }
        val segments = path.split('/').filter { it.isNotEmpty() }

        return when {
            path == ManagementMainNavigation.Main.route -> ManagementMainNavigation.Main

            path == CommonNavigationRoutes.BASE_SETTINGS_ROUTE || path == ManagementSettingsNavigation.Main.route ->
                ManagementSettingsNavigation.Main

            path == ManagementSettingsNavigation.GlobalSettings.route ->
                ManagementSettingsNavigation.GlobalSettings

            path == ManagementAuthSettingsNavigation.AuthSettings.route ->
                ManagementAuthSettingsNavigation.AuthSettings

            path == ManagementSecurityNavigation.SecuritySettings.route ->
                ManagementSecurityNavigation.SecuritySettings

            path == ManagementUsersNavigation.BASE_USERS_ROUTE || path == ManagementUsersNavigation.UserList.ROUTE ->
                ManagementUsersNavigation.UserList(ManagementUserListParams.parse(queryMap))

            path == ManagementUsersNavigation.CreateUser.route ->
                ManagementUsersNavigation.CreateUser

            segments.size >= 2 && "/${segments[0]}" == ManagementUsersNavigation.BASE_USERS_ROUTE -> {
                val userId = segments[1].toUserIdOrNull()
                    ?: return ManagementUsersNavigation.UserList(ManagementUserListParams.parse(queryMap))
                val subPath = segments.getOrNull(2)
                when (subPath) {
                    SESSIONS_SEGMENT -> ManagementUsersNavigation.UserSessionList(ManagementUserSessionListParams(userId = userId.asHexDashString()))
                    IDENTIFIERS_SEGMENT -> ManagementUsersNavigation.UserIdentifierList(ManagementUserIdentifierListParams(userId = userId.asHexDashString()))
                    else -> ManagementUsersNavigation.UserDetail(UserDetailParams(userId = userId.asHexDashString()))
                }
            }

            path == ManagementAuditNavigation.Main.route || path == ManagementAuditNavigation.AuditEventList.ROUTE ->
                ManagementAuditNavigation.AuditEventList(ManagementAuditEventListParams.parse(queryMap))

            segments.size >= 2 && "/${segments[0]}" == ManagementAuditNavigation.AuditEventList.ROUTE -> {
                val eventId = segments[1].toAuditEventIdOrNull()
                    ?: return ManagementAuditNavigation.AuditEventList(ManagementAuditEventListParams.parse(queryMap))
                ManagementAuditNavigation.AuditEventDetail(ManagementAuditEventDetailParams(eventId = eventId.asHexDashString()))
            }

            path == ManagementUsersNavigation.BASE_SESSIONS_ROUTE || path == ManagementUsersNavigation.GlobalSessionList.ROUTE ->
                ManagementUsersNavigation.GlobalSessionList(ManagementGlobalSessionListParams.parse(queryMap))

            segments.size >= 2 && "/${segments[0]}" == ManagementUsersNavigation.BASE_SESSIONS_ROUTE -> {
                val sessionId = segments[1].toUserSessionIdOrNull()
                    ?: return ManagementUsersNavigation.GlobalSessionList(ManagementGlobalSessionListParams.parse(queryMap))
                ManagementUsersNavigation.SessionDetail(SessionDetailParams(sessionId = sessionId.asHexDashString()))
            }

            path == ManagementUsersNavigation.BASE_IDENTIFIERS_ROUTE || path == ManagementUsersNavigation.GlobalIdentifierList.ROUTE ->
                ManagementUsersNavigation.GlobalIdentifierList(ManagementGlobalIdentifierListParams.parse(queryMap))

            segments.size >= 2 && "/${segments[0]}" == ManagementUsersNavigation.BASE_IDENTIFIERS_ROUTE -> {
                val identifierId = segments[1].toUserIdentifierIdOrNull()
                    ?: return ManagementUsersNavigation.GlobalIdentifierList(ManagementGlobalIdentifierListParams.parse(queryMap))
                ManagementUsersNavigation.IdentifierDetail(IdentifierDetailParams(identifierId = identifierId.asHexDashString()))
            }

            else -> null
        }
    }

    private companion object {
        const val MANAGEMENT_PREFIX = "/management"
        const val SESSIONS_SEGMENT = "sessions"
        const val IDENTIFIERS_SEGMENT = "identifiers"
    }
}
