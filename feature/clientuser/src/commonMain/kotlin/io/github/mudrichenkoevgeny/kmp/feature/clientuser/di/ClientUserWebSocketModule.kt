package io.github.mudrichenkoevgeny.kmp.feature.clientuser.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.feature.user.network.websocket.messagehandler.UserWebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken.RefreshTokenUseCase
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope

/**
 * Lazily constructs the user WebSocket message handler from a shared [HttpClient].
 *
 * @param userStorage User-scoped local storage.
 * @param authStorage Token storage for clearing credentials.
 * @param refreshTokenUseCase Triggers token refresh on authentication expiry frames.
 * @param scope Coroutine scope for storage/network update tasks.
 */
class ClientUserWebSocketModule(
    private val userStorage: UserStorage,
    private val authStorage: AuthStorage,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val scope: CoroutineScope
) {
    /**
     * Component that handles user-related push messages from the server.
     */
    val userWebSocketMessageHandler: WebSocketMessageHandler by lazy {
        UserWebSocketMessageHandler(
            userStorage = userStorage,
            authStorage = authStorage,
            refreshTokenUseCase = refreshTokenUseCase,
            scope = scope,
        )
    }
}
