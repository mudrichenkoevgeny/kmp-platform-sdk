package io.github.mudrichenkoevgeny.kmp.feature.user.model.user

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserRole
import kotlin.time.Instant

data class CurrentUser(
    val id: UserId,
    val role: UserRole,
    val accountStatus: UserAccountStatus,
    val lastLoginAt: Instant?,
    val lastActiveAt: Instant?,
    val createdAt: Instant,
    val updatedAt: Instant?
)