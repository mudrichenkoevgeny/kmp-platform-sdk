package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.settings.network.api.globalsettings.GlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings.GlobalSettingsRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.GlobalSettingsStorage
import kotlinx.coroutines.CoroutineScope

internal class SettingsRepositoryModule(
    private val globalSettingsApi: GlobalSettingsApi,
    private val globalSettingsStorage: GlobalSettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) {
    val globalSettingsRepository by lazy {
        GlobalSettingsRepositoryImpl(
            globalSettingsApi = globalSettingsApi,
            globalSettingsStorage = globalSettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }
}