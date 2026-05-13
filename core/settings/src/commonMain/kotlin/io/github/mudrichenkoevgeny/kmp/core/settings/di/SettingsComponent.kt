package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.GlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.GlobalSettingsStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Root wiring component for `core/settings`.
 *
 * Assembles storage layer for global settings. Exposes:
 * - [GlobalSettingsStorage] (`globalSettingsStorage`)
 *
 * Constructor dependencies:
 * - [EncryptedSettings]: backing store for encrypted global settings persistence.
 * - `parentScope`: optional scope for repository coroutines; if null, a supervisor scope on the default dispatcher is created.
 */
class SettingsComponent(
    webSocketService: WebSocketService,
    globalSettingsApi: GlobalSettingsApi,
    encryptedSettings: EncryptedSettings,
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

    private val repositoryModule by lazy {
        SettingsRepositoryModule(
            globalSettingsApi = globalSettingsApi,
            globalSettingsStorage = globalSettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = componentScope
        )
    }
    val globalSettingsRepository get() = repositoryModule.globalSettingsRepository

    private val webSocketsModule by lazy {
        SettingsWebSocketsModule(
            globalSettingsRepository = globalSettingsRepository,
            scope = componentScope
        )
    }
    val settingsWebSocketMessageHandler get() = webSocketsModule.settingsWebSocketMessageHandler


    private val useCaseModule by lazy {
        SettingsUseCaseModule(
            globalSettingsRepository = globalSettingsRepository
        )
    }
    val refreshGlobalSettingsUseCase get() = useCaseModule.refreshGlobalSettingsUseCase
    val getGlobalSettingsUseCase get() = useCaseModule.getGlobalSettingsUseCase
}