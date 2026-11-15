package io.github.mudrichenkoevgeny.kmp.core.common.network.provider

import kotlinx.coroutines.flow.StateFlow

/**
 * Provides the current access token for authenticated SDK network requests.
 *
 * The token value is observed by the WebSocket service to restart the connection when it changes.
 */
public interface AccessTokenProvider {
    /**
     * Current access token stream.
     *
     * The value can be `null` when the user is not authenticated.
     */
    public val accessTokenFlow: StateFlow<String?>
}
