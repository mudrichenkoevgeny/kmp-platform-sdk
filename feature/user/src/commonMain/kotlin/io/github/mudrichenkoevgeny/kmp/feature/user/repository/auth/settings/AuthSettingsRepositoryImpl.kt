package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings

import co.touchlab.kermit.Logger
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.settings.toAuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.settings.AuthSettingsApi
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.shared.foundation.core.common.serialization.FoundationJson
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.contract.UserWebSocketEventTypes
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.settings.AuthSettingsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.decodeFromJsonElement

class AuthSettingsRepositoryImpl(
    private val authSettingsApi: AuthSettingsApi,
    private val authStorage: AuthStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) : AuthSettingsRepository {

    private val updateMutex = Mutex()
    private val _settings = MutableStateFlow<AuthSettings?>(null)

    init {
        repositoryScope.launch {
            _settings.value = authStorage.getAuthSettings()

            webSocketService.observeEvents()
                .filter { it.type == UserWebSocketEventTypes.AUTH_SETTINGS_UPDATED }
                .collect { frame ->
                    try {
                        val response = frame.payload?.let {
                            FoundationJson.decodeFromJsonElement<AuthSettingsResponse>(it)
                        }

                        if (response != null) {
                            updateAuthSettings(response.toAuthSettings())
                        } else {
                            Logger.w { "Received AUTH_SETTINGS_UPDATED with invalid payload" }
                        }
                    } catch (e: Exception) {
                        Logger.e(e) { "Failed to process WebSocket event" }
                    }
                }
        }
    }

    override suspend fun getAuthSettings(): AppResult<AuthSettings> {
        _settings.value?.let { return AppResult.Success(it) }

        return updateMutex.withLock {
            val cached = _settings.value ?: authStorage.getAuthSettings()

            if (cached != null) {
                _settings.value = cached
                AppResult.Success(cached)
            } else {
                refreshAuthSettingsInternal()
            }
        }
    }

    override suspend fun refreshAuthSettings(): AppResult<AuthSettings> {
        return updateMutex.withLock {
            refreshAuthSettingsInternal()
        }
    }

    override suspend fun updateAuthSettings(authSettings: AuthSettings) {
        updateMutex.withLock {
            applySettingsUpdate(authSettings)
        }
    }

    override fun observeAuthSettings(): Flow<AuthSettings?> = _settings.asStateFlow()

    private suspend fun refreshAuthSettingsInternal(): AppResult<AuthSettings> {
        return authSettingsApi.getAuthSettings()
            .mapSuccess { response ->
                val settings = response.toAuthSettings()
                applySettingsUpdate(settings)
                settings
            }
    }

    private suspend fun applySettingsUpdate(authSettings: AuthSettings) {
        authStorage.updateAuthSettings(authSettings)
        _settings.value = authSettings
    }
}