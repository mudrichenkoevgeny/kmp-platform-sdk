package io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.permission.PermissionCode
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlin.time.Clock
import kotlin.time.Instant

@InternalApi
fun userDetailsMock(
    id: UserId = UserId.generate(),
    role: UserRole = UserRole.USER,
    accountStatus: UserAccountStatus = UserAccountStatus.ACTIVE,
    accountStatusBeforeDeletion: UserAccountStatus? = null,
    authorityLevel: Int = 0,
    permissionCodes: Set<PermissionCode> = emptySet(),
    isTotpEnabled: Boolean = false,
    lastLoginAt: Instant? = null,
    lastActiveAt: Instant? = null,
    createdAt: Instant = Clock.System.now(),
    updatedAt: Instant? = null,
    scheduledPermanentDeletionAt: Instant? = null
) = UserDetails(
    id = id,
    role = role,
    accountStatus = accountStatus,
    accountStatusBeforeDeletion = accountStatusBeforeDeletion,
    authorityLevel = authorityLevel,
    permissionCodes = permissionCodes,
    isTotpEnabled = isTotpEnabled,
    lastLoginAt = lastLoginAt,
    lastActiveAt = lastActiveAt,
    createdAt = createdAt,
    updatedAt = updatedAt,
    scheduledPermanentDeletionAt = scheduledPermanentDeletionAt
)