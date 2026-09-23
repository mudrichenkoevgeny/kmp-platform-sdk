package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrThrow
import kotlinx.serialization.Serializable

/**
 * Navigation destinations for the management settings stack.
 */
@Serializable
sealed class ManagementSettingsDestination {
    /** Main menu with edit action buttons. */
    @Serializable
    object Main : ManagementSettingsDestination()

    /** Screen for editing authentication settings. */
    @Serializable
    object EditAuthSettings : ManagementSettingsDestination()

    /** Screen for editing global platform settings. */
    @Serializable
    object EditGlobalSettings : ManagementSettingsDestination()

    /** Screen for editing security policies and settings. */
    @Serializable
    object EditSecuritySettings : ManagementSettingsDestination()

    /** Screen for users management. */
    @Serializable
    object UsersManagement : ManagementSettingsDestination()

    /** Screen for viewing audit logs. */
    @Serializable
    object AuditLogs : ManagementSettingsDestination()

    /** Screen for viewing all platform sessions. */
    @Serializable
    object GlobalSessionList : ManagementSettingsDestination()

    /** Screen for viewing session details. */
    @Serializable
    data class SessionDetail(
        val sessionIdValue: String
    ) : ManagementSettingsDestination() {
        val sessionId: UserSessionId get() = sessionIdValue.toUserSessionIdOrThrow()
    }
}
