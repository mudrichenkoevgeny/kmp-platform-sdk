package io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth

import io.github.mudrichenkoevgeny.kmp.core.common.network.provider.AccessTokenProvider
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.AccessToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.RefreshToken
import kotlinx.coroutines.flow.StateFlow

/**
 * Persists session tokens and cached auth settings for the user feature. Implementations are supplied by the host
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

    /**
     * Persists a new session after login or refresh.
     *
     * @param accessToken New bearer access token.
     * @param refreshToken New refresh token.
     * @param expiresAt Access token expiry as epoch milliseconds.
     */
    suspend fun updateTokens(accessToken: AccessToken, refreshToken: RefreshToken, expiresAt: Long)

    /** Removes tokens from storage (logout / invalid session). */
    suspend fun clearTokens()

    /** @return Last known auth settings snapshot, or null if never loaded. */
    suspend fun getAuthSettings(): AuthSettings?

    /** @param authSettings Replaces cached provider/policy settings from the backend or WebSocket. */
    suspend fun updateAuthSettings(authSettings: AuthSettings)

    /** Clears cached auth settings (e.g. on logout). */
    suspend fun clearAuthSettings()
}