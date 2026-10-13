package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.userlist

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.toUserIdOrThrow
import kotlinx.serialization.Serializable

/**
 * Navigation arguments for user identifier list screen.
 */
@Serializable
data class UserIdentifierListParams(
    val userIdValue: String
) {
    val userId: UserId get() = userIdValue.toUserIdOrThrow()
}
