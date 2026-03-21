package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.settings.network.api.globalsettings.GlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings.GlobalSettingsRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.GlobalSettingsStorage
import kotlinx.coroutines.CoroutineScope

/**
 * Internal repository wiring for `core/settings`.
 *
 * Coordinates [GlobalSettingsApi], [GlobalSettingsStorage], and [WebSocketService] into a single
 * [GlobalSettingsRepository] implementation.
 */
internal class SettingsRepositoryModule(
    private val globalSettingsApi: GlobalSettingsApi,
    private val globalSettingsStorage: GlobalSettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) {
    /**
     * Default [GlobalSettingsRepository] for this module.
     */
    val globalSettingsRepository by lazy {
        GlobalSettingsRepositoryImpl(
            globalSettingsApi = globalSettingsApi,
            globalSettingsStorage = globalSettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }
}