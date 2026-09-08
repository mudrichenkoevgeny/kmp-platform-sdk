package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.di

import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.RefreshManagementGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.RefreshManagementSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.RefreshManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.usecase.SyncManagementDataUseCase

/**
 * Internal sample wiring that builds [SyncManagementDataUseCase] from module refresh use cases.
 */
internal class ManagementAppUseCaseModule(
    refreshManagementGlobalSettingsUseCase: RefreshManagementGlobalSettingsUseCase,
    refreshManagementSecuritySettingsUseCase: RefreshManagementSecuritySettingsUseCase,
    refreshManagementAuthSettingsUseCase: RefreshManagementAuthSettingsUseCase
) {
    /**
     * Parallel refresh of global, security, and auth settings for startup-style sync.
     */
    val syncManagementDataUseCase by lazy {
        SyncManagementDataUseCase(
            refreshManagementGlobalSettingsUseCase = refreshManagementGlobalSettingsUseCase,
            refreshManagementSecuritySettingsUseCase = refreshManagementSecuritySettingsUseCase,
            refreshManagementAuthSettingsUseCase = refreshManagementAuthSettingsUseCase
        )
    }
}
