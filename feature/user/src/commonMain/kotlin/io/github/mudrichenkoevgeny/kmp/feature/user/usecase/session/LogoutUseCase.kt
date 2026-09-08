package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage

/**
 * Ends the active session on the server (best effort) and clears local storage regardless of network outcome.
 *
 * @param sessionRepository Remote session management API.
 * @param authStorage Token storage to be cleared.
 * @param userStorage User profile storage to be cleared.
 */
open class LogoutUseCase(
    private val sessionRepository: SessionRepository,
    private val authStorage: AuthStorage,
    private val userStorage: UserStorage
) {
    /**
     * Clears local storage and returns success regardless of whether remote logout succeeds or fails.
     *
     * @return Success result after local tokens and user data are cleared.
     */
    open suspend operator fun invoke(): AppResult<Unit> {
        try {
            sessionRepository.logout()
        } catch (_: Exception) {
        } finally {
            authStorage.clearTokens()
            userStorage.clear()
        }
        return AppResult.Success(Unit)
    }
}
