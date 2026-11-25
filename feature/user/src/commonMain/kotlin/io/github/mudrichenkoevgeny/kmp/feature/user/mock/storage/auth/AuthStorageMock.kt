package io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId
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
public class AuthStorageMock : AuthStorage {

    private var accessToken: AccessToken? = null
    private var refreshToken: RefreshToken? = null
    private var expiresAt: Long = 0L
    private var sessionId: UserSessionId? = null
    private var identifierId: UserIdentifierId? = null

    public var isTokensCleared: Boolean = false
        private set

    private val _accessTokenFlow = MutableStateFlow<String?>(null)
    public override val accessTokenFlow: StateFlow<String?> = _accessTokenFlow.asStateFlow()

    public override suspend fun getAccessToken(): AccessToken? = accessToken

    public override suspend fun getRefreshToken(): RefreshToken? = refreshToken

    public override suspend fun getExpiresAt(): Long = expiresAt

    public override suspend fun getSessionId(): UserSessionId? = sessionId

    public override suspend fun getIdentifierId(): UserIdentifierId? = identifierId

    public override suspend fun updateTokens(sessionToken: SessionToken) {
        this.accessToken = sessionToken.accessToken
        this.refreshToken = sessionToken.refreshToken
        this.expiresAt = sessionToken.expiresAt.toEpochMilliseconds()
        this.sessionId = sessionToken.sessionId
        this.identifierId = sessionToken.identifierId
        _accessTokenFlow.value = sessionToken.accessToken.value
    }

    public override suspend fun clearTokens() {
        accessToken = null
        refreshToken = null
        expiresAt = 0L
        sessionId = null
        identifierId = null
        _accessTokenFlow.value = null
        isTokensCleared = true
    }
}
