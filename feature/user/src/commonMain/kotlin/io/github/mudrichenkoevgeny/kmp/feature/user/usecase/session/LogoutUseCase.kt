package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository

/**
 * Ends the active session on the server (best effort) and clears local user session regardless of network outcome.
 *
 * @param sessionRepository Remote session management API.
 * @param userRepository User repository for clearing local session state.
 */
open class LogoutUseCase(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) {
    /**
     * Clears local session and returns success regardless of whether remote logout succeeds or fails.
     *
     * @return Success result after local tokens and user data are cleared.
     */
    open suspend operator fun invoke(): AppResult<Unit> {
        try {
            sessionRepository.logout()
        } catch (_: Exception) {
        } finally {
            userRepository.clearSession()
        }
        return AppResult.Success(Unit)
    }
}
