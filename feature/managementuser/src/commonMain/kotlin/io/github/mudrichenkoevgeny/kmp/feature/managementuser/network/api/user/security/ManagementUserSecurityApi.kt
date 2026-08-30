package io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/** Administrative security management operations for user accounts. */
interface ManagementUserSecurityApi {

    /**
     * Administratively disables TOTP (2FA) for a specific user.
     *
     * @param userId Unique identifier of the target user.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend fun disableTotp(userId: UserId): AppResult<Unit>
}