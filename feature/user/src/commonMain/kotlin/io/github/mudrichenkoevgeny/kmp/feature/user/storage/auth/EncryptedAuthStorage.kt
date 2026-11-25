package io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth

import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.toUserIdentifierIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.toUserSessionIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.AccessToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.RefreshToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.SessionToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock

/**
 * Production [AuthStorage] that persists session tokens under encrypted keys and mirrors the access token
 * string into [accessTokenFlow].
 *
 * @param encryptedSettings Host-provided encrypted settings.
 * @param scope [CoroutineScope] used to load the initial access token from storage on construction (updates [accessTokenFlow]).
 */
public class EncryptedAuthStorage(
    private val encryptedSettings: EncryptedSettings,
    scope: CoroutineScope
) : AuthStorage, AccessTokenProvider {

    private val _accessTokenFlow = MutableStateFlow<String?>(null)
    public override val accessTokenFlow: StateFlow<String?> = _accessTokenFlow.asStateFlow()

    init {
        scope.launch {
            val expiresAt = encryptedSettings.get(KEY_EXPIRES_AT)?.toLongOrNull() ?: 0L
            val now = Clock.System.now().toEpochMilliseconds()
            if (expiresAt in 1..now) {
                clearTokens()
            } else {
                val accessToken = encryptedSettings.get(KEY_ACCESS_TOKEN)
                _accessTokenFlow.value = accessToken
            }
        }
    }

    public override suspend fun getAccessToken(): AccessToken? {
        val expiresAt = getExpiresAt()
        val now = Clock.System.now().toEpochMilliseconds()
        if (expiresAt in 1..now) {
            clearTokens()
            return null
        }
        return encryptedSettings.get(KEY_ACCESS_TOKEN)?.let { AccessToken(it) }
    }

    public override suspend fun getRefreshToken(): RefreshToken? =
        encryptedSettings.get(KEY_REFRESH_TOKEN)?.let { RefreshToken(it) }

    public override suspend fun getExpiresAt(): Long =
        encryptedSettings.get(KEY_EXPIRES_AT)?.toLongOrNull() ?: 0L

    public override suspend fun getSessionId(): UserSessionId? =
        encryptedSettings.get(KEY_SESSION_ID)?.let { it.toUserSessionIdOrNull() }

    public override suspend fun getIdentifierId(): UserIdentifierId? =
        encryptedSettings.get(KEY_IDENTIFIER_ID)?.let { it.toUserIdentifierIdOrNull() }

    public override suspend fun updateTokens(sessionToken: SessionToken) {
        encryptedSettings.put(KEY_ACCESS_TOKEN, sessionToken.accessToken.value)
        encryptedSettings.put(KEY_REFRESH_TOKEN, sessionToken.refreshToken.value)
        encryptedSettings.put(KEY_EXPIRES_AT, sessionToken.expiresAt.toEpochMilliseconds().toString())
        encryptedSettings.put(KEY_SESSION_ID, sessionToken.sessionId.asHexDashString())
        encryptedSettings.put(KEY_IDENTIFIER_ID, sessionToken.identifierId.asHexDashString())
        _accessTokenFlow.value = sessionToken.accessToken.value
    }

    public override suspend fun clearTokens() {
        encryptedSettings.remove(KEY_ACCESS_TOKEN)
        encryptedSettings.remove(KEY_REFRESH_TOKEN)
        encryptedSettings.remove(KEY_EXPIRES_AT)
        encryptedSettings.remove(KEY_SESSION_ID)
        encryptedSettings.remove(KEY_IDENTIFIER_ID)
        if (_accessTokenFlow.value != null) {
            _accessTokenFlow.value = null
        }
    }

    private companion object {
        private const val KEY_ACCESS_TOKEN = "auth_access_token"
        private const val KEY_REFRESH_TOKEN = "auth_refresh_token"
        private const val KEY_EXPIRES_AT = "auth_expires_at"
        private const val KEY_SESSION_ID = "auth_session_id"
        private const val KEY_IDENTIFIER_ID = "auth_identifier_id"
    }
}
