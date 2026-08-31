package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository

/**
 * Performs re-authentication via TOTP for the current session to update its trust level.
 *
 * @param sessionRepository Remote session management API.
 */
class ReauthenticateSessionUseCase(
    private val sessionRepository: SessionRepository
) {
    /**
     * @param mfaToken Opaque intermediate verification token.
     * @param code time-based verification code.
     * @return Empty success indicator, or a mapped failure.
     */
    suspend operator fun invoke(mfaToken: String, code: String): AppResult<Unit> {
        return sessionRepository.reauthenticateSession(mfaToken, code)
    }
}
