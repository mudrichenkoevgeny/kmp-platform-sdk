package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.settings.network.api.globalsettings.GlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.settings.network.websockets.messagehandler.SettingsWebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.GlobalSettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.GetGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.RefreshGlobalSettingsUseCase
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Root wiring component for `core/settings`.
 *
 * Assembles storage, network, repository, and use-case layers for global settings. Exposes:
 * - [GlobalSettingsStorage] (`globalSettingsStorage`)
 * - [GlobalSettingsApi] (`globalSettingsApi`) and [SettingsWebSocketMessageHandler] for host registration
 * - [GlobalSettingsRepository] (`globalSettingsRepository`)
 * - [RefreshGlobalSettingsUseCase] and [GetGlobalSettingsUseCase]
 *
 * Constructor dependencies:
 * - [EncryptedSettings]: backing store for encrypted global settings persistence.
 * - [HttpClient]: shared Ktor client (typically from `core/common`) for REST calls.
 * - [WebSocketService]: allows the global settings repository to observe server-driven settings updates.
 * - `parentScope`: optional scope for repository coroutines; if null, a supervisor scope on the default dispatcher is created.
 */
class SettingsComponent(
    encryptedSettings: EncryptedSettings,
    httpClient: HttpClient,
    webSocketService: WebSocketService,
    parentScope: CoroutineScope? = null
) {
    private val componentScope = parentScope
        ?: CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val storageModule by lazy {
        SettingsStorageModule(
            encryptedSettings
        )
    }
    val globalSettingsStorage get() = storageModule.globalSettingsStorage

    private val networkModule by lazy {
        SettingsNetworkModule(
            httpClient
        )
    }
    val globalSettingsApi get() = networkModule.globalSettingsApi
    val settingsWebSocketMessageHandler get() = networkModule.settingsWebSocketMessageHandler

    private val repositoryModule by lazy {
        SettingsRepositoryModule(
            globalSettingsApi = globalSettingsApi,
            globalSettingsStorage = globalSettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = componentScope
        )
    }
    val globalSettingsRepository get() = repositoryModule.globalSettingsRepository

    private val useCaseModule by lazy {
        SettingsUseCaseModule(
            globalSettingsRepository = globalSettingsRepository
        )
    }
    val refreshGlobalSettingsUseCase get() = useCaseModule.refreshGlobalSettingsUseCase
    val getGlobalSettingsUseCase get() = useCaseModule.getGlobalSettingsUseCase
}