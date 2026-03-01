package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

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