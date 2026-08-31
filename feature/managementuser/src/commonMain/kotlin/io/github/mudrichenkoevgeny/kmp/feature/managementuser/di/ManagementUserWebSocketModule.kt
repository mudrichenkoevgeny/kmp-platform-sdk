package io.github.mudrichenkoevgeny.kmp.feature.managementuser.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.feature.user.network.websocket.messagehandler.UserWebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken.RefreshTokenUseCase
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope

/**
 * Lazily constructs the user WebSocket message handler from a shared [HttpClient].
 */
/**
 * Lazily constructs the user WebSocket message handler from a shared [HttpClient].
 *
 * @param userStorage User snapshot cache.
 * @param authStorage Token storage.
 * @param refreshTokenUseCase Use case for auto-reauth.
 * @param scope Coroutine scope for push updates.
 */
class ManagementUserWebSocketModule(
    private val userStorage: UserStorage,
    private val authStorage: AuthStorage,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val scope: CoroutineScope
) {
    val userWebSocketMessageHandler: WebSocketMessageHandler by lazy {
        UserWebSocketMessageHandler(
            userStorage = userStorage,
            authStorage = authStorage,
            refreshTokenUseCase = refreshTokenUseCase,
            scope = scope,
        )
    }
}
