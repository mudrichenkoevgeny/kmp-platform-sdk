package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.GetGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.RefreshGlobalSettingsUseCase

internal class SettingsUseCaseModule(
    globalSettingsRepository: GlobalSettingsRepository
) {
    val refreshGlobalSettingsUseCase by lazy {
        RefreshGlobalSettingsUseCase(
            globalSettingsRepository
        )
    }

    val getGlobalSettingsUseCase by lazy {
        GetGlobalSettingsUseCase(
            globalSettingsRepository
        )
    }
}