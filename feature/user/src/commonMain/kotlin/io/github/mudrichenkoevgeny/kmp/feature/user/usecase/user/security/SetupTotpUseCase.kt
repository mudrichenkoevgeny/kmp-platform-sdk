package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.security.UserSecurityRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup

/**
 * Initiates the TOTP setup process by generating a secret key and a configuration URI.
 *
 * @param userSecurityRepository Remote security management API.
 */
class SetupTotpUseCase(
    private val userSecurityRepository: UserSecurityRepository
) {
    /**
     * @return [TotpSetup] details on success, or a mapped failure.
     */
    suspend operator fun invoke(): AppResult<TotpSetup> {
        return userSecurityRepository.setupTotp()
    }
}
