package io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth

import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.AccessToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.RefreshToken

class EncryptedAuthStorage(
    private val encryptedSettings: EncryptedSettings
) : AuthStorage {

    override suspend fun getAccessToken(): AccessToken? =
        encryptedSettings.get(KEY_ACCESS_TOKEN)?.let { AccessToken(it) }

    override suspend fun getRefreshToken(): RefreshToken? =
        encryptedSettings.get(KEY_REFRESH_TOKEN)?.let { RefreshToken(it) }

    override suspend fun getExpiresAt(): Long =
        encryptedSettings.get(KEY_EXPIRES_AT)?.toLongOrNull() ?: 0L

    override suspend fun updateTokens(
        accessToken: AccessToken,
        refreshToken: RefreshToken,
        expiresAt: Long
    ) {
        encryptedSettings.put(KEY_ACCESS_TOKEN, accessToken.value)
        encryptedSettings.put(KEY_REFRESH_TOKEN, refreshToken.value)
        encryptedSettings.put(KEY_EXPIRES_AT, expiresAt.toString())
    }

    override suspend fun clear() {
        encryptedSettings.remove(KEY_ACCESS_TOKEN)
        encryptedSettings.remove(KEY_REFRESH_TOKEN)
        encryptedSettings.remove(KEY_EXPIRES_AT)
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "auth_access_token"
        private const val KEY_REFRESH_TOKEN = "auth_refresh_token"
        private const val KEY_EXPIRES_AT = "auth_expires_at"
    }
}