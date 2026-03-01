package io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth

import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.AccessToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.RefreshToken
import kotlinx.coroutines.flow.StateFlow

interface AuthStorage : AccessTokenProvider {
    override val accessTokenFlow: StateFlow<String?>
    suspend fun getAccessToken(): AccessToken?
    suspend fun getRefreshToken(): RefreshToken?
    suspend fun getExpiresAt(): Long
    suspend fun updateTokens(accessToken: AccessToken, refreshToken: RefreshToken, expiresAt: Long)
    suspend fun clearTokens()

    suspend fun getAuthSettings(): AuthSettings?
    suspend fun updateAuthSettings(authSettings: AuthSettings)
    suspend fun clearAuthSettings()
}