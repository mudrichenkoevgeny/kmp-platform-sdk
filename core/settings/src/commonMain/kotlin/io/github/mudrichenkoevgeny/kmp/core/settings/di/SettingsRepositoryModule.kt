package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.settings.storage.globalsettings.OpenGlobalSettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.OpenGlobalSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.OpenGlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.OpenGlobalSettingsRepositoryImpl
import kotlinx.coroutines.CoroutineScope

/**
 * Internal repository wiring for `core/settings`.
 *
 * Coordinates [OpenGlobalSettingsApi], [OpenGlobalSettingsStorage], and [WebSocketService] into a single
 * [OpenGlobalSettingsRepository] implementation.
 */
internal class SettingsRepositoryModule(
    private val openGlobalSettingsApi: OpenGlobalSettingsApi,
    private val openGlobalSettingsStorage: OpenGlobalSettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) {
    /**
     * Default [OpenGlobalSettingsRepository] for this module.
     */
    val openGlobalSettingsRepository by lazy {
        OpenGlobalSettingsRepositoryImpl(
            openGlobalSettingsApi = openGlobalSettingsApi,
            openGlobalSettingsStorage = openGlobalSettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }
}