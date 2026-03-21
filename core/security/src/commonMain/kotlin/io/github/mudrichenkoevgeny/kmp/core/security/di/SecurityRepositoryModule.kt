package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.security.network.api.securitysettings.SecuritySettingsApi
import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.SecuritySettingsStorage
import kotlinx.coroutines.CoroutineScope

/**
 * Internal repository wiring for `core/security`.
 *
 * Coordinates [SecuritySettingsApi], [SecuritySettingsStorage], and [WebSocketService] into a single
 * [SecuritySettingsRepository] implementation.
 */
internal class SecurityRepositoryModule(
    private val securitySettingsApi: SecuritySettingsApi,
    private val securitySettingsStorage: SecuritySettingsStorage,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) {
    /**
     * Default [SecuritySettingsRepository] for this module.
     */
    val securitySettingsRepository by lazy {
        SecuritySettingsRepositoryImpl(
            securitySettingsApi = securitySettingsApi,
            securitySettingsStorage = securitySettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }
}