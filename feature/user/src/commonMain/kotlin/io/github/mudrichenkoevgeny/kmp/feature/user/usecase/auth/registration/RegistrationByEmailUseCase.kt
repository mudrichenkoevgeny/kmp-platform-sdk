package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration.RegistrationRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage

/**
 * Registers a new account by email and, on success, stores session tokens and the new user snapshot.
 *
 * @param registrationRepository Remote registration API.
 * @param authStorage Encrypted token storage updated after successful registration.
 * @param userStorage User snapshot storage updated after successful registration.
 */
class RegistrationByEmailUseCase(
    private val registrationRepository: RegistrationRepository,
    private val authStorage: AuthStorage,
    private val userStorage: UserStorage
) {
    /**
     * @param email New account email.
     * @param password Chosen password.
     * @param confirmationCode Code from the registration confirmation email.
     * @return [AuthData] after successful registration and local persistence, or an error result
     * without updating storage when registration fails.
     */
    suspend fun execute(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<AuthData> {
        return registrationRepository.registerByEmail(
            email = email,
            password = password,
            confirmationCode = confirmationCode
        ).onSuccess { authData ->
            authStorage.updateTokens(
                accessToken = authData.sessionToken.accessToken,
                refreshToken = authData.sessionToken.refreshToken,
                expiresAt = authData.sessionToken.expiresAt
            )
            userStorage.updateCurrentUser(authData.currentUser)
        }
    }
}