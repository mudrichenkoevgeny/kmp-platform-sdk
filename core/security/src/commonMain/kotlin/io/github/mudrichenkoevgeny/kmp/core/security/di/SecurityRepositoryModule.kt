package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.OpenSecuritySettingsApi
import io.github.mudrichenkoevgeny.kmp.core.security.repository.OpenSecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.security.repository.OpenSecuritySettingsRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.OpenSecuritySettingsStorage
import kotlinx.coroutines.CoroutineScope

/**
 * Internal repository wiring for `core/security`.
 *
 * Coordinates [OpenSecuritySettingsApi], [OpenSecuritySettingsStorage], and [WebSocketService] into a single
 * [OpenSecuritySettingsRepository] implementation.
 */
internal class SecurityRepositoryModule(
    private val openSecuritySettingsApi: OpenSecuritySettingsApi,
    private val openSecuritySettingsStorage: OpenSecuritySettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) {
    /**
     * Default [OpenSecuritySettingsRepository] for this module.
     */
    val openSecuritySettingsRepository by lazy {
        OpenSecuritySettingsRepositoryImpl(
            openSecuritySettingsApi = openSecuritySettingsApi,
            openSecuritySettingsStorage = openSecuritySettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }
}