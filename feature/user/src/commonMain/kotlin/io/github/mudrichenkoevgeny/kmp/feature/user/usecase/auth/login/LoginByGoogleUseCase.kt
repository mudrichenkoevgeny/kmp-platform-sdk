package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.flatMap
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider

class LoginByGoogleUseCase(
    private val authService: GoogleAuthService,
    private val loginRepository: LoginRepository,
    private val authStorage: AuthStorage,
    private val userStorage: UserStorage
) {
    suspend fun execute(): AppResult<AuthData> {
        return authService.signIn()
            .flatMap { authToken ->
                loginRepository.loginByExternalAuthProvider(UserAuthProvider.GOOGLE, authToken)
            }
            .onSuccess { authData ->
                authStorage.updateTokens(
                    accessToken = authData.sessionToken.accessToken,
                    refreshToken = authData.sessionToken.refreshToken,
                    expiresAt = authData.sessionToken.expiresAt
                )
                userStorage.updateCurrentUser(authData.currentUser)
            }
    }
}