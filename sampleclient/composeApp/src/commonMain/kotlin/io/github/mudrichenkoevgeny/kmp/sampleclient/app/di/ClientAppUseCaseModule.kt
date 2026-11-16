package io.github.mudrichenkoevgeny.kmp.sampleclient.app.di

import io.github.mudrichenkoevgeny.kmp.core.security.usecase.RefreshOpenSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.RefreshOpenGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.RefreshOpenAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.usecase.SyncClientDataRunner

/**
 * Internal sample wiring that builds [SyncClientDataRunner] from module refresh use cases.
 */
internal class ClientAppUseCaseModule(
    refreshOpenGlobalSettingsUseCase: RefreshOpenGlobalSettingsUseCase,
    refreshOpenSecuritySettingsUseCase: RefreshOpenSecuritySettingsUseCase,
    refreshOpenAuthSettingsUseCase: RefreshOpenAuthSettingsUseCase
) {
    /**
     * Parallel refresh of global, security, and auth settings for startup-style sync.
     */
    val syncClientDataRunner by lazy {
        SyncClientDataRunner(
            refreshOpenGlobalSettingsUseCase = refreshOpenGlobalSettingsUseCase,
            refreshOpenSecuritySettingsUseCase = refreshOpenSecuritySettingsUseCase,
            refreshOpenAuthSettingsUseCase = refreshOpenAuthSettingsUseCase
        )
    }
}
