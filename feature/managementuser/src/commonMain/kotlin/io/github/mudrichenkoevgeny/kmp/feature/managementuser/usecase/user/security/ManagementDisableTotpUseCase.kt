package io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.security.ManagementUserSecurityRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserId

/**
 * Administratively disables TOTP (2FA) for a specific user.
 *
 * @param managementUserSecurityRepository Administrative security management API.
 */
public class ManagementDisableTotpUseCase(
    private val managementUserSecurityRepository: ManagementUserSecurityRepository
) {
    /**
     * @param userId Unique identifier of the target user.
     * @return Empty success indicator, or a mapped failure.
     */
    public suspend operator fun invoke(userId: UserId): AppResult<Unit> = managementUserSecurityRepository.disableTotp(
        userId
    )
}
