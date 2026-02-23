package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.SessionToken
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken.RefreshTokenRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage

class RefreshTokenUseCase(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val authStorage: AuthStorage
) {
    suspend fun execute(): AppResult<SessionToken> {
        val currentRefreshToken = authStorage.getRefreshToken()?.value
            ?: return AppResult.Error(UserError.InvalidRefreshToken())

        return refreshTokenRepository.refreshToken(currentRefreshToken)
            .onSuccess { sessionToken ->
                authStorage.updateTokens(
                    accessToken = sessionToken.accessToken,
                    refreshToken = sessionToken.refreshToken,
                    expiresAt = sessionToken.expiresAt
                )
            }
    }
}