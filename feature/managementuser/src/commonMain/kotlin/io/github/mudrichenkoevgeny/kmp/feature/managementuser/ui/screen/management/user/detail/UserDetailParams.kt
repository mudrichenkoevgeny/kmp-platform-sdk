package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.toUserIdOrThrow
import kotlinx.serialization.Serializable

/**
 * Navigation arguments for user detail screen.
 */
@Serializable
data class UserDetailParams(
    val userIdValue: String
) {
    val userId: UserId get() = userIdValue.toUserIdOrThrow()
}
