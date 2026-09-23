package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.toUserIdOrThrow
import kotlinx.serialization.Serializable

@Serializable
sealed class UsersManagementDestination {
    @Serializable
    object Main : UsersManagementDestination()

    @Serializable
    data class Detail(val userIdValue: String) : UsersManagementDestination() {
        val userId: UserId get() = userIdValue.toUserIdOrThrow()
    }

    @Serializable
    object Create : UsersManagementDestination()

    @Serializable
    data class UserSessionList(val userIdValue: String) : UsersManagementDestination() {
        val userId: UserId get() = userIdValue.toUserIdOrThrow()
    }

    @Serializable
    data class SessionDetail(
        val userIdValue: String,
        val sessionIdValue: String
    ) : UsersManagementDestination() {
        val userId: UserId get() = userIdValue.toUserIdOrThrow()
        val sessionId: UserSessionId get() = sessionIdValue.toUserSessionIdOrThrow()
    }

    @Serializable
    data class Identifiers(val userIdValue: String) : UsersManagementDestination() {
        val userId: UserId get() = userIdValue.toUserIdOrThrow()
    }
}
