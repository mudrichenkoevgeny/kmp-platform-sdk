package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.toUserIdentifierIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.toUserIdOrThrow
import kotlinx.serialization.Serializable

/**
 * Navigation destinations for the users management stack.
 */
@Serializable
sealed class UsersManagementDestination {
    
    /** Main screen with the list of users. */
    @Serializable
    object Main : UsersManagementDestination()

    /** Screen for viewing user details. */
    @Serializable
    data class Detail(val userIdValue: String) : UsersManagementDestination() {
        val userId: UserId get() = userIdValue.toUserIdOrThrow()
    }

    /** Screen for creating a new user. */
    @Serializable
    object Create : UsersManagementDestination()

    /** Screen for viewing a specific user's sessions. */
    @Serializable
    data class UserSessionList(val userIdValue: String) : UsersManagementDestination() {
        val userId: UserId get() = userIdValue.toUserIdOrThrow()
    }

    /** Screen for viewing session details. */
    @Serializable
    data class SessionDetail(
        val userIdValue: String,
        val sessionIdValue: String
    ) : UsersManagementDestination() {
        val userId: UserId get() = userIdValue.toUserIdOrThrow()
        val sessionId: UserSessionId get() = sessionIdValue.toUserSessionIdOrThrow()
    }

    /** Screen for viewing a specific user's identifiers. */
    @Serializable
    data class Identifiers(val userIdValue: String) : UsersManagementDestination() {
        val userId: UserId get() = userIdValue.toUserIdOrThrow()
    }

    /** Screen for viewing identifier details. */
    @Serializable
    data class IdentifierDetail(
        val userIdValue: String,
        val identifierIdValue: String
    ) : UsersManagementDestination() {
        val userId: UserId get() = userIdValue.toUserIdOrThrow()
        val identifierId: UserIdentifierId get() = identifierIdValue.toUserIdentifierIdOrThrow()
    }
}
