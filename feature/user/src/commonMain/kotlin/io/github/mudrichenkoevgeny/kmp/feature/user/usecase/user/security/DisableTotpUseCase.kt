package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.security.UserSecurityRepository

/**
 * Disables TOTP multifactor authentication for the current account.
 *
 * @param userSecurityRepository Remote security management API.
 */
class DisableTotpUseCase(
    private val userSecurityRepository: UserSecurityRepository
) {
    /**
     * @return Empty success indicator, or a mapped failure.
     */
    suspend operator fun invoke(): AppResult<Unit> {
        return userSecurityRepository.disableTotp()
    }
}
