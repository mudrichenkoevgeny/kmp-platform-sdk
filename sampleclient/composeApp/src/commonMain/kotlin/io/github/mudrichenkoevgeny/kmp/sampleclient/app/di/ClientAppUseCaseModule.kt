package io.github.mudrichenkoevgeny.kmp.sampleclient.app.di

import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.RefreshGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.RefreshSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.usecase.auth.settings.RefreshAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.usecase.SyncDataUseCase

/**
 * Internal sample wiring that builds [SyncDataUseCase] from module refresh use cases.
 */
internal class ClientAppUseCaseModule(
    refreshGlobalSettingsUseCase: RefreshGlobalSettingsUseCase,
    refreshSecuritySettingsUseCase: RefreshSecuritySettingsUseCase,
    refreshAuthSettingsUseCase: RefreshAuthSettingsUseCase
) {
    /**
     * Parallel refresh of global, security, and auth settings for startup-style sync.
     */
    val syncDataUseCase by lazy {
        SyncDataUseCase(
            refreshGlobalSettingsUseCase = refreshGlobalSettingsUseCase,
            refreshSecuritySettingsUseCase = refreshSecuritySettingsUseCase,
            refreshAuthSettingsUseCase = refreshAuthSettingsUseCase
        )
    }
}