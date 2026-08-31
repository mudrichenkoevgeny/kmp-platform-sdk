package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.security.ManagementUserSecurityRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Administratively disables TOTP (2FA) for a specific user.
 *
 * @param managementUserSecurityRepository Administrative security management API.
 */
class ManagementDisableTotpUseCase(
    private val managementUserSecurityRepository: ManagementUserSecurityRepository
) {
    /**
     * @param userId Unique identifier of the target user.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend operator fun invoke(userId: UserId): AppResult<Unit> {
        return managementUserSecurityRepository.disableTotp(userId)
    }
}
