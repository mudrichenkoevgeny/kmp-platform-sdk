package io.github.mudrichenkoevgeny.kmp.feature.user.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.feature.user.network.websocket.messagehandler.UserWebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.AuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken.RefreshTokenUseCase
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlin.getValue

/**
 * Lazily constructs the user WebSocket message handler from a shared [HttpClient].
 */
internal class UserWebSocketModule(
    private val userStorage: UserStorage,
    private val authStorage: AuthStorage,
    private val authSettingsRepository: AuthSettingsRepository,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val scope: CoroutineScope
) {
    val userWebSocketMessageHandler: WebSocketMessageHandler by lazy {
        UserWebSocketMessageHandler(
            userStorage = userStorage,
            authStorage = authStorage,
            authSettingsRepository = authSettingsRepository,
            refreshTokenUseCase = refreshTokenUseCase,
            scope = scope,
        )
    }
}