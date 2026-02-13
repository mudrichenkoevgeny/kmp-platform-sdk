package io.github.mudrichenkoevgeny.kmp.feature.user.model.user

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserRole

data class PublicUser(
    val id: UserId,
    val role: UserRole,
    val accountStatus: UserAccountStatus
)