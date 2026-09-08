package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.OpenGlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.OpenGlobalSettingsStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Root wiring component for `core/settings`.
 *
 * Assembles storage layer for global settings. Exposes:
 * - [OpenGlobalSettingsStorage] (`globalSettingsStorage`)
 *
 * Constructor dependencies:
 * - [EncryptedSettings]: backing store for encrypted global settings persistence.
 * - `parentScope`: optional scope for repository coroutines; if null, a supervisor scope on the default dispatcher is created.
 */
class SettingsComponent(
    webSocketService: WebSocketService,
    openGlobalSettingsApi: OpenGlobalSettingsApi,
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
    val globalSettingsStorage get() = storageModule.openGlobalSettingsStorage

    private val repositoryModule by lazy {
        SettingsRepositoryModule(
            openGlobalSettingsApi = openGlobalSettingsApi,
            openGlobalSettingsStorage = globalSettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = componentScope
        )
    }
    val globalSettingsRepository get() = repositoryModule.openGlobalSettingsRepository

    private val webSocketsModule by lazy {
        SettingsWebSocketsModule()
    }
    val settingsWebSocketMessageHandler get() = webSocketsModule.settingsWebSocketMessageHandler


    private val useCaseModule by lazy {
        SettingsUseCaseModule(
            openGlobalSettingsRepository = globalSettingsRepository
        )
    }
    val refreshGlobalSettingsUseCase get() = useCaseModule.refreshOpenGlobalSettingsUseCase
    val getGlobalSettingsUseCase get() = useCaseModule.getOpenGlobalSettingsUseCase
}