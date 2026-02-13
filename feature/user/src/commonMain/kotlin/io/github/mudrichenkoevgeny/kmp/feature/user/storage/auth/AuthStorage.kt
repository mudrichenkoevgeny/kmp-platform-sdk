package io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth

import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.AccessToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.RefreshToken

interface AuthStorage {
    suspend fun getAccessToken(): AccessToken?
    suspend fun getRefreshToken(): RefreshToken?
    suspend fun getExpiresAt(): Long
    suspend fun updateTokens(accessToken: AccessToken, refreshToken: RefreshToken, expiresAt: Long)
    suspend fun clear()
}