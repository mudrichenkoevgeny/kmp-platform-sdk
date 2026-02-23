package io.github.mudrichenkoevgeny.kmp.sample.app.di

import io.github.mudrichenkoevgeny.kmp.core.security.usecase.RefreshSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.RefreshGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.RefreshAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.sample.app.usecase.SyncDataUseCase

internal class AppUseCaseModule(
    refreshGlobalSettingsUseCase: RefreshGlobalSettingsUseCase,
    refreshSecuritySettingsUseCase: RefreshSecuritySettingsUseCase,
    refreshAuthSettingsUseCase: RefreshAuthSettingsUseCase
) {
    val syncDataUseCase by lazy {
        SyncDataUseCase(
            refreshGlobalSettingsUseCase = refreshGlobalSettingsUseCase,
            refreshSecuritySettingsUseCase = refreshSecuritySettingsUseCase,
            refreshAuthSettingsUseCase = refreshAuthSettingsUseCase
        )
    }
}