package io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.AccessToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.RefreshToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.SessionToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * In-memory [AuthStorage] for tests and previews. Token fields and [accessTokenFlow] stay in sync, mirroring
 * `EncryptedAuthStorage` behavior without persistence.
 */
@InternalApi
class AuthStorageMock : AuthStorage {

    private var accessToken: AccessToken? = null
    private var refreshToken: RefreshToken? = null
    private var expiresAt: Long = 0L
    private var sessionId: String? = null
    private var identifierId: String? = null

    var isTokensCleared = false
        private set

    private val _accessTokenFlow = MutableStateFlow<String?>(null)
    override val accessTokenFlow: StateFlow<String?> = _accessTokenFlow.asStateFlow()

    override suspend fun getAccessToken(): AccessToken? = accessToken

    override suspend fun getRefreshToken(): RefreshToken? = refreshToken

    override suspend fun getExpiresAt(): Long = expiresAt

    override suspend fun getSessionId(): String? = sessionId

    override suspend fun getIdentifierId(): String? = identifierId

    override suspend fun updateTokens(sessionToken: SessionToken) {
        this.accessToken = sessionToken.accessToken
        this.refreshToken = sessionToken.refreshToken
        this.expiresAt = sessionToken.expiresAt.toEpochMilliseconds()
        this.sessionId = sessionToken.sessionId.asHexDashString()
        this.identifierId = sessionToken.identifierId.asHexDashString()
        _accessTokenFlow.value = sessionToken.accessToken.value
    }

    override suspend fun clearTokens() {
        accessToken = null
        refreshToken = null
        expiresAt = 0L
        sessionId = null
        identifierId = null
        _accessTokenFlow.value = null
        isTokensCleared = true
    }
}
