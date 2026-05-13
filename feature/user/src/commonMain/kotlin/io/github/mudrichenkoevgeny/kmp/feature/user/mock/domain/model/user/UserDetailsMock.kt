package io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import kotlin.time.Clock

@InternalApi
fun userDetailsMock() = UserDetails(
    role = UserRole.USER,
    accountStatus = UserAccountStatus.ACTIVE,
    accountStatusBeforeDeletion = null,
    authorityLevel = 0,
    permissionCodes = emptySet(),
    isTotpEnabled = false,
    createdAt = Clock.System.now(),
)