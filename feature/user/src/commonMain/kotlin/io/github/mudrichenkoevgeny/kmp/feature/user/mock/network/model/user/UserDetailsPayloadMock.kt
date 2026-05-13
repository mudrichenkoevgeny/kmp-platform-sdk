package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserDetailsPayload

@InternalApi
fun userDetailsPayloadMock(
    id: String = "123e4567-e89b-12d3-a456-426614174000",
    createdAtEpochMs: Long = 1L
): UserDetailsPayload = UserDetailsPayload(
    id = id,
    role = UserRole.USER.serialName,
    accountStatus = UserAccountStatus.ACTIVE.serialName,
    accountStatusBeforeDeletion = null,
    authorityLevel = 0,
    permissionCodes = emptySet(),
    isTotpEnabled = false,
    lastLoginAt = null,
    lastActiveAt = null,
    createdAt = createdAtEpochMs,
    updatedAt = null,
    scheduledPermanentDeletionAt = null
)