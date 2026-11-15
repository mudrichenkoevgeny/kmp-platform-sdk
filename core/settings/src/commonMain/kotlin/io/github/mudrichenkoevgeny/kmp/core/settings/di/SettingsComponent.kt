package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.OpenGlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.OpenGlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.OpenGlobalSettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.GetOpenGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.ObserveOpenGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.RefreshOpenGlobalSettingsUseCase
import io.ktor.client.HttpClient
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
public class SettingsComponent(
    webSocketService: WebSocketService,
    httpClient: HttpClient,
    encryptedSettings: EncryptedSettings,
    openGlobalSettingsApi: OpenGlobalSettingsApi? = null,
    parentScope: CoroutineScope? = null
) {
    private val componentScope = parentScope
        ?: CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val storageModule by lazy {
        SettingsStorageModule(
            encryptedSettings
        )
    }
    public val globalSettingsStorage: OpenGlobalSettingsStorage get() = storageModule.openGlobalSettingsStorage

    private val networkModule by lazy {
        SettingsNetworkModule(httpClient)
    }

    private val repositoryModule by lazy {
        SettingsRepositoryModule(
            openGlobalSettingsApi = openGlobalSettingsApi ?: networkModule.openGlobalSettingsApi,
            openGlobalSettingsStorage = globalSettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = componentScope
        )
    }
    public val globalSettingsRepository: OpenGlobalSettingsRepository
        get() =
            repositoryModule.openGlobalSettingsRepository

    private val webSocketsModule by lazy {
        SettingsWebSocketsModule()
    }
    public val settingsWebSocketMessageHandler: WebSocketMessageHandler
        get() = webSocketsModule.settingsWebSocketMessageHandler

    private val useCaseModule by lazy {
        SettingsUseCaseModule(
            openGlobalSettingsRepository = globalSettingsRepository
        )
    }
    public val refreshGlobalSettingsUseCase: RefreshOpenGlobalSettingsUseCase
        get() = useCaseModule.refreshOpenGlobalSettingsUseCase
    public val getGlobalSettingsUseCase: GetOpenGlobalSettingsUseCase
        get() = useCaseModule.getOpenGlobalSettingsUseCase
    public val observeGlobalSettingsUseCase: ObserveOpenGlobalSettingsUseCase
        get() = useCaseModule.observeOpenGlobalSettingsUseCase
}
