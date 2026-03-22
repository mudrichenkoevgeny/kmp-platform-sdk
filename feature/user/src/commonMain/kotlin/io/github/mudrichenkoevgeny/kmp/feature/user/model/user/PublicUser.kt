package io.github.mudrichenkoevgeny.kmp.feature.user.model.user

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserRole

/** Non-sensitive user profile fields suitable for caches and UI outside a full auth payload. */
data class PublicUser(
    val id: UserId,
    val role: UserRole,
    val accountStatus: UserAccountStatus
)