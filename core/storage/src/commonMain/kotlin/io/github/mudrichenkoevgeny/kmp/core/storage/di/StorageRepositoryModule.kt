package io.github.mudrichenkoevgeny.kmp.core.storage.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.storagesettings.OpenStorageSettingsApi
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.settings.OpenStorageSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.settings.OpenStorageSettingsRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.core.storage.storage.storagesettings.OpenStorageSettingsStorage
import kotlinx.coroutines.CoroutineScope

/**
 * Internal repository wiring for `core/storage`.
 *
 * Coordinates [OpenStorageSettingsApi], [OpenStorageSettingsStorage], and [WebSocketService] into a single
 * [OpenStorageSettingsRepository] implementation.
 */
internal class StorageRepositoryModule(
    private val openStorageSettingsApi: OpenStorageSettingsApi,
    private val openStorageSettingsStorage: OpenStorageSettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) {
    /**
     * Default [OpenStorageSettingsRepository] for this module.
     */
    val openStorageSettingsRepository by lazy {
        OpenStorageSettingsRepositoryImpl(
            openStorageSettingsApi = openStorageSettingsApi,
            openStorageSettingsStorage = openStorageSettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }
}
