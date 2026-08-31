package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage

/**
 * Ends the current active session on the server and clears local storage on success.
 *
 * @param sessionRepository Remote session management API.
 * @param authStorage Token storage to be cleared.
 * @param userStorage User profile storage to be cleared.
 */
class LogoutUseCase(
    private val sessionRepository: SessionRepository,
    private val authStorage: AuthStorage,
    private val userStorage: UserStorage
) {
    /**
     * @return Empty success indicator and cleared storage on success, or a mapped failure.
     */
    suspend operator fun invoke(): AppResult<Unit> {
        return sessionRepository.logout()
            .onSuccess {
                authStorage.clearTokens()
                userStorage.clear()
            }
    }
}
