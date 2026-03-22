package io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth

import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.AccessToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.RefreshToken
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Production [AuthStorage] that persists tokens and cached [AuthSettings] under encrypted keys and mirrors the access token
 * string into [accessTokenFlow].
 *
 * @param encryptedSettings Host-provided encrypted settings.
 * @param scope [CoroutineScope] used to load the initial access token from storage on construction (updates [accessTokenFlow]).
 */
class EncryptedAuthStorage(
    private val encryptedSettings: EncryptedSettings,
    scope: CoroutineScope
) : AuthStorage, AccessTokenProvider {

    private val json = FoundationJson

    private val _accessTokenFlow = MutableStateFlow<String?>(null)
    override val accessTokenFlow: StateFlow<String?> = _accessTokenFlow.asStateFlow()

    init {
        scope.launch {
            val accessToken = encryptedSettings.get(KEY_ACCESS_TOKEN)
            _accessTokenFlow.value = accessToken
        }
    }

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
        _accessTokenFlow.value = accessToken.value
    }

    override suspend fun clearTokens() {
        encryptedSettings.remove(KEY_ACCESS_TOKEN)
        encryptedSettings.remove(KEY_REFRESH_TOKEN)
        encryptedSettings.remove(KEY_EXPIRES_AT)
        _accessTokenFlow.emit(null)
    }

    override suspend fun getAuthSettings(): AuthSettings? {
        val data = encryptedSettings.get(KEY_AUTH_SETTINGS)
            ?: return null
        return json.decodeFromString(data)
    }

    override suspend fun updateAuthSettings(authSettings: AuthSettings) {
        val data = json.encodeToString(authSettings)
        encryptedSettings.put(KEY_AUTH_SETTINGS, data)
    }

    override suspend fun clearAuthSettings() {
        encryptedSettings.remove(KEY_AUTH_SETTINGS)
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "auth_access_token"
        private const val KEY_REFRESH_TOKEN = "auth_refresh_token"
        private const val KEY_EXPIRES_AT = "auth_expires_at"

        private const val KEY_AUTH_SETTINGS = "auth_settings"
    }
}