package io.github.mudrichenkoevgeny.kmp.feature.managementuser.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.core.common.navigation.deeplink.UrlFormatter
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.ManagementDestination
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root.ManagementRootComponent
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments

class ManagementUrlFormatter(
    private val baseHost: String = ManagementDeepLinkConstants.DEFAULT_BASE_HOST
) : UrlFormatter<ManagementDestination> {

    override fun format(destination: ManagementDestination): String {
        val builder = URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = baseHost
        )

        when (destination) {
            is ManagementDestination.Main -> builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT)
            is ManagementDestination.EditAuthSettings -> {
                builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.SETTINGS, ManagementDeepLinkConstants.Segment.AUTH)
                destination.params.dialog?.let { dialog ->
                    builder.parameters.append(ManagementDeepLinkConstants.Query.PARAM_DIALOG, dialog)
                }
            }
            is ManagementDestination.EditGlobalSettings -> {
                builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.SETTINGS, ManagementDeepLinkConstants.Segment.GLOBAL)
                destination.params.dialog?.let { dialog ->
                    builder.parameters.append(ManagementDeepLinkConstants.Query.PARAM_DIALOG, dialog)
                }
            }
            is ManagementDestination.EditSecuritySettings -> {
                builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.SETTINGS, ManagementDeepLinkConstants.Segment.SECURITY)
                destination.params.dialog?.let { dialog ->
                    builder.parameters.append(ManagementDeepLinkConstants.Query.PARAM_DIALOG, dialog)
                }
            }
            is ManagementDestination.GlobalUserList -> builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.USERS)
            is ManagementDestination.CreateUser -> builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.USERS, ManagementDeepLinkConstants.Segment.CREATE)
            is ManagementDestination.UserDetail -> builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.USERS, destination.userId.asHexDashString())
            is ManagementDestination.UserSessionList -> builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.USERS, destination.userId.asHexDashString(), ManagementDeepLinkConstants.Segment.SESSIONS)
            is ManagementDestination.UserIdentifierList -> builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.USERS, destination.userId.asHexDashString(), ManagementDeepLinkConstants.Segment.IDENTIFIERS)
            is ManagementDestination.AuditEventList -> {
                builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.AUDIT)
                destination.params.initialFilters?.actorIds?.forEach { actorId ->
                    builder.parameters.append(ManagementDeepLinkConstants.Query.PARAM_ACTOR_IDS, actorId)
                }
            }
            is ManagementDestination.AuditEventDetail -> builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.AUDIT, destination.eventId.asHexDashString())
            is ManagementDestination.GlobalSessionList -> builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.SESSIONS)
            is ManagementDestination.SessionDetail -> builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.SESSIONS, destination.sessionId.asHexDashString())
            is ManagementDestination.GlobalIdentifierList -> builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.IDENTIFIERS)
            is ManagementDestination.IdentifierDetail -> builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.IDENTIFIERS, destination.identifierId.asHexDashString())
        }

        return builder.buildString()
    }

    fun formatChild(destination: ManagementDestination, child: ManagementRootComponent.Child): String {
        return when (child) {
            is ManagementRootComponent.Child.EditAuthSettings -> {
                if (child.component.dialogSlot.value.child?.instance != null) {
                    val builder = URLBuilder(protocol = URLProtocol.HTTPS, host = baseHost)
                    builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.SETTINGS, ManagementDeepLinkConstants.Segment.AUTH)
                    builder.parameters.append(ManagementDeepLinkConstants.Query.PARAM_DIALOG, ManagementDeepLinkConstants.Value.DIALOG_RESET_CONFIRM)
                    builder.buildString()
                } else {
                    format(destination)
                }
            }
            is ManagementRootComponent.Child.EditGlobalSettings -> {
                if (child.component.dialogSlot.value.child?.instance != null) {
                    val builder = URLBuilder(protocol = URLProtocol.HTTPS, host = baseHost)
                    builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.SETTINGS, ManagementDeepLinkConstants.Segment.GLOBAL)
                    builder.parameters.append(ManagementDeepLinkConstants.Query.PARAM_DIALOG, ManagementDeepLinkConstants.Value.DIALOG_RESET_CONFIRM)
                    builder.buildString()
                } else {
                    format(destination)
                }
            }
            is ManagementRootComponent.Child.EditSecuritySettings -> {
                if (child.component.dialogSlot.value.child?.instance != null) {
                    val builder = URLBuilder(protocol = URLProtocol.HTTPS, host = baseHost)
                    builder.appendPathSegments(ManagementDeepLinkConstants.Segment.ROOT, ManagementDeepLinkConstants.Segment.SETTINGS, ManagementDeepLinkConstants.Segment.SECURITY)
                    builder.parameters.append(ManagementDeepLinkConstants.Query.PARAM_DIALOG, ManagementDeepLinkConstants.Value.DIALOG_RESET_CONFIRM)
                    builder.buildString()
                } else {
                    format(destination)
                }
            }
            else -> format(destination)
        }
    }
}
