package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.security.UserSecurityRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes

/**
 * Retrieves the active backup recovery codes for the current account.
 *
 * @param userSecurityRepository Remote security management API.
 */
open class GetRecoveryCodesUseCase(
    private val userSecurityRepository: UserSecurityRepository
) {
    /**
     * @return Existing [TotpRecoveryCodes] details on success, or a mapped failure.
     */
    open suspend operator fun invoke(): AppResult<TotpRecoveryCodes> {
        return userSecurityRepository.getRecoveryCodes()
    }
}
