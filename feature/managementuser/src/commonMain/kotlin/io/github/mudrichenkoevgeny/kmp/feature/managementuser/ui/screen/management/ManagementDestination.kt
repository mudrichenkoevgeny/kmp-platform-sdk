package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management

import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail.AuditEventDetailParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list.AuditEventListParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist.UserIdentifierListParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist.UserSessionListParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsParams
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailParams
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.serialization.Serializable

/**
 * Navigation destinations for the management stack.
 */
@Serializable
sealed class ManagementDestination {
    /** Main menu with action buttons. */
    @Serializable
    object Main : ManagementDestination()

    /** Screen for editing authentication settings. */
    @Serializable
    data class EditAuthSettings(
        val params: SettingsParams = SettingsParams()
    ) : ManagementDestination()

    /** Screen for editing global platform settings. */
    @Serializable
    data class EditGlobalSettings(
        val params: SettingsParams = SettingsParams()
    ) : ManagementDestination()

    /** Screen for editing security policies and settings. */
    @Serializable
    data class EditSecuritySettings(
        val params: SettingsParams = SettingsParams()
    ) : ManagementDestination()

    /** Screen for viewing all platform users. */
    @Serializable
    object GlobalUserList : ManagementDestination()

    /** Screen for creating a new user. */
    @Serializable
    object CreateUser : ManagementDestination()

    /** Screen for viewing user details. */
    @Serializable
    data class UserDetail(
        val params: UserDetailParams
    ) : ManagementDestination() {
        val userId: UserId get() = params.userId
    }

    /** Screen for viewing user sessions. */
    @Serializable
    data class UserSessionList(
        val params: UserSessionListParams
    ) : ManagementDestination() {
        val userId: UserId get() = params.userId
    }

    /** Screen for viewing user identifiers. */
    @Serializable
    data class UserIdentifierList(
        val params: UserIdentifierListParams
    ) : ManagementDestination() {
        val userId: UserId get() = params.userId
    }

    /** Screen for viewing audit logs. */
    @Serializable
    data class AuditEventList(
        val params: AuditEventListParams = AuditEventListParams()
    ) : ManagementDestination()

    /** Screen for viewing audit event details. */
    @Serializable
    data class AuditEventDetail(
        val params: AuditEventDetailParams
    ) : ManagementDestination() {
        val eventId: AuditEventId get() = params.eventId
    }

    /** Screen for viewing all platform sessions. */
    @Serializable
    object GlobalSessionList : ManagementDestination()

    /** Screen for viewing session details. */
    @Serializable
    data class SessionDetail(
        val params: SessionDetailParams
    ) : ManagementDestination() {
        val sessionId: UserSessionId get() = params.sessionId
    }

    /** Screen for viewing all platform identifiers. */
    @Serializable
    object GlobalIdentifierList : ManagementDestination()

    /** Screen for viewing identifier details. */
    @Serializable
    data class IdentifierDetail(
        val params: IdentifierDetailParams
    ) : ManagementDestination() {
        val identifierId: UserIdentifierId get() = params.identifierId
    }
}