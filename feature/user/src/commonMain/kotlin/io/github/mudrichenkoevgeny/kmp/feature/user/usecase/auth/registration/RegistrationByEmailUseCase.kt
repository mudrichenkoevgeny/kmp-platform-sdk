package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration.RegistrationRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage

class RegistrationByEmailUseCase(
    private val registrationRepository: RegistrationRepository,
    private val authStorage: AuthStorage,
    private val userStorage: UserStorage
) {
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