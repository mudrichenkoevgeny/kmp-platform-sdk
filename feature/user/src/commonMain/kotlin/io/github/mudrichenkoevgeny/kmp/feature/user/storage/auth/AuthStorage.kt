package io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth

import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.AccessToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.RefreshToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.SessionToken
import kotlinx.coroutines.flow.StateFlow

/**
 * Persists session tokens for the user feature. Implementations are supplied by the host
 * (e.g. encrypted DataStore) and are read by the user HTTP client auth setup and use cases.
 */
interface AuthStorage : AccessTokenProvider {
    /** Raw access token string for observers; mirrors the latest non-null session token value. */
    override val accessTokenFlow: StateFlow<String?>

    /** @return Parsed access token, or null if absent. */
    suspend fun getAccessToken(): AccessToken?

    /** @return Parsed refresh token, or null if absent. */
    suspend fun getRefreshToken(): RefreshToken?

    /** @return Access token expiry instant as epoch milliseconds. */
    suspend fun getExpiresAt(): Long

    /** @return The current active session ID, or null if absent. */
    suspend fun getSessionId(): String?

    /** @return The identifier ID used to authorize this session, or null if absent. */
    suspend fun getIdentifierId(): String?

    /**
     * Persists a new session after login or refresh.
     *
     * @param sessionToken New session token containing access/refresh tokens and session IDs.
     */
    suspend fun updateTokens(sessionToken: SessionToken)

    /** Removes tokens from storage (logout / invalid session). */
    suspend fun clearTokens()
}
