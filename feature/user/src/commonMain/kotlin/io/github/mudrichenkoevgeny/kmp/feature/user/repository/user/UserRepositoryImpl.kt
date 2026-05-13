package io.github.mudrichenkoevgeny.kmp.feature.user.repository.user

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandlerResult
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.user.UserApi
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.user.toUserDetails
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.user.UserDetailsPayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * Implements [UserRepository] by observing [UserStorage], coordinating network updates via [UserApi],
 * and listening for live [UserWebSocketEventTypes.USER_UPDATED] events via [WebSocketService].
 *
 * @param userStorage Local source for the current user stream and persistence.
 * @param userApi User HTTP API for profile and account management.
 * @param webSocketService Source of push updates for user changes.
 * @param repositoryScope Scope used to collect WebSocket events throughout the repository's lifecycle.
 */
class UserRepositoryImpl(
    private val userStorage: UserStorage,
    private val authStorage: AuthStorage,
    private val userApi: UserApi,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : UserRepository {

    private val mutex = Mutex()

    override val currentUser: Flow<UserDetails?> = userStorage.observeCurrentUser()

    init {
        repositoryScope.launch {
            webSocketService.observeEvents()
                .collect { webSocketFrame ->
                    when (webSocketFrame.type) {
                        UserWebSocketEventTypes.USER_UPDATED -> {
                            handleUserUpdated(webSocketFrame.payload)
                        }
                        UserWebSocketEventTypes.SESSION_DELETED -> {
                            handleSessionDeleted()
                        }
                    }
                }
        }
    }

    override suspend fun refreshCurrentUser(): AppResult<UserDetails> {
        return mutex.withLock {
            userApi.getUser().mapSuccess { userDetailsPayload: UserDetailsPayload ->
                val userDetails = userDetailsPayload.toUserDetails()
                userStorage.updateCurrentUser(userDetails)
                userDetails
            }
        }
    }

    override suspend fun scheduleUserDeletion(): AppResult<UserDetails> {
        return mutex.withLock {
            userApi.scheduleUserDeletion().mapSuccess { userDetailsPayload: UserDetailsPayload ->
                val userDetails = userDetailsPayload.toUserDetails()
                userStorage.updateCurrentUser(userDetails)
                userDetails
            }
        }
    }

    override suspend fun restoreUser(): AppResult<UserDetails> {
        return mutex.withLock {
            userApi.restoreUser().mapSuccess { userDetailsPayload: UserDetailsPayload ->
                val userDetails = userDetailsPayload.toUserDetails()
                userStorage.updateCurrentUser(userDetails)
                userDetails
            }
        }
    }

    private suspend fun handleUserUpdated(payload: JsonElement?): WebSocketMessageHandlerResult {
        if (payload == null) {
            return WebSocketMessageHandlerResult.Error(CommonError.ContractViolation())
        }

        val userDetailsPayload = try {
            FoundationJson.decodeFromJsonElement<UserDetailsPayload>(payload)
        } catch (e: Exception) {
            return WebSocketMessageHandlerResult.Error(CommonError.ContractViolation(e))
        }

        userStorage.updateCurrentUser(userDetailsPayload.toUserDetails())

        return WebSocketMessageHandlerResult.Handled
    }

    private suspend fun handleSessionDeleted() {
        Logger.i { "Received SESSION_DELETED: clearing local user and auth data" }
        userStorage.clear()
        authStorage.clearTokens()
    }
}