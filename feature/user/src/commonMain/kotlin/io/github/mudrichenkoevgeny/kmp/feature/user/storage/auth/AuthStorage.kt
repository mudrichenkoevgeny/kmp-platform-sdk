package io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth

import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.user.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.AccessToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.RefreshToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.SessionToken
import kotlinx.coroutines.flow.StateFlow

/**
 * Persists session tokens for the user feature. Implementations are supplied by the host
 * (e.g. encrypted DataStore) and are read by the user HTTP client auth setup and use cases.
 */
public interface AuthStorage : AccessTokenProvider {
    /** Raw access token string for observers; mirrors the latest non-null session token value. */
    public override val accessTokenFlow: StateFlow<String?>

    /** @return Parsed access token, or null if absent. */
    public suspend fun getAccessToken(): AccessToken?

    /** @return Parsed refresh token, or null if absent. */
    public suspend fun getRefreshToken(): RefreshToken?

    /** @return Access token expiry instant as epoch milliseconds. */
    public suspend fun getExpiresAt(): Long

    /** @return The current active session ID, or null if absent. */
    public suspend fun getSessionId(): UserSessionId?

    /** @return The identifier ID used to authorize this session, or null if absent. */
    public suspend fun getIdentifierId(): UserIdentifierId?

    /**
     * Persists a new session after login or refresh.
     *
     * @param sessionToken New session token containing access/refresh tokens and session IDs.
     */
    public suspend fun updateTokens(sessionToken: SessionToken)

    /** Removes tokens from storage (logout / invalid session). */
    public suspend fun clearTokens()
}
