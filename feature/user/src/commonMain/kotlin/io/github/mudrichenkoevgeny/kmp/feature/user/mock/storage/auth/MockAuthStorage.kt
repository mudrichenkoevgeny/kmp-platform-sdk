package io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth

import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.AccessToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.RefreshToken
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockAuthStorage : AuthStorage {

    private var accessToken: AccessToken? = null
    private var refreshToken: RefreshToken? = null
    private var expiresAt: Long = 0L
    private var authSettings: AuthSettings? = null

    override val accessTokenFlow: StateFlow<String?> = MutableStateFlow(null).asStateFlow()

    override suspend fun getAccessToken(): AccessToken? = accessToken

    override suspend fun getRefreshToken(): RefreshToken? = refreshToken

    override suspend fun getExpiresAt(): Long = expiresAt

    override suspend fun updateTokens(accessToken: AccessToken, refreshToken: RefreshToken, expiresAt: Long) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        this.expiresAt = expiresAt
    }

    override suspend fun clearTokens() {
        accessToken = null
        refreshToken = null
        expiresAt = 0L
    }

    override suspend fun getAuthSettings(): AuthSettings? = authSettings

    override suspend fun updateAuthSettings(authSettings: AuthSettings) {
        this.authSettings = authSettings
    }

    override suspend fun clearAuthSettings() {
        authSettings = null
    }
}