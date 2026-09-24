package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management

import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.toAuditEventIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.toUserIdentifierIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.toUserIdOrThrow
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
    object EditAuthSettings : ManagementDestination()

    /** Screen for editing global platform settings. */
    @Serializable
    object EditGlobalSettings : ManagementDestination()

    /** Screen for editing security policies and settings. */
    @Serializable
    object EditSecuritySettings : ManagementDestination()

    /** Screen for viewing all platform users. */
    @Serializable
    object GlobalUserList : ManagementDestination()

    /** Screen for creating a new user. */
    @Serializable
    object CreateUser : ManagementDestination()

    /** Screen for viewing user details. */
    @Serializable
    data class UserDetail(
        val userIdValue: String
    ) : ManagementDestination() {
        val userId: UserId get() = userIdValue.toUserIdOrThrow()
    }

    /** Screen for viewing user sessions. */
    @Serializable
    data class UserSessionList(
        val userIdValue: String
    ) : ManagementDestination() {
        val userId: UserId get() = userIdValue.toUserIdOrThrow()
    }

    /** Screen for viewing user identifiers. */
    @Serializable
    data class UserIdentifierList(
        val userIdValue: String
    ) : ManagementDestination() {
        val userId: UserId get() = userIdValue.toUserIdOrThrow()
    }

    /** Screen for viewing audit logs. */
    @Serializable
    object AuditEventList : ManagementDestination()

    /** Screen for viewing audit event details. */
    @Serializable
    data class AuditEventDetail(
        val eventIdValue: String
    ) : ManagementDestination() {
        val eventId: AuditEventId get() = eventIdValue.toAuditEventIdOrThrow()
    }

    /** Screen for viewing all platform sessions. */
    @Serializable
    object GlobalSessionList : ManagementDestination()

    /** Screen for viewing session details. */
    @Serializable
    data class SessionDetail(
        val sessionIdValue: String
    ) : ManagementDestination() {
        val sessionId: UserSessionId get() = sessionIdValue.toUserSessionIdOrThrow()
    }

    /** Screen for viewing all platform identifiers. */
    @Serializable
    object GlobalIdentifierList : ManagementDestination()

    /** Screen for viewing identifier details. */
    @Serializable
    data class IdentifierDetail(
        val identifierIdValue: String
    ) : ManagementDestination() {
        val identifierId: UserIdentifierId get() = identifierIdValue.toUserIdentifierIdOrThrow()
    }
}