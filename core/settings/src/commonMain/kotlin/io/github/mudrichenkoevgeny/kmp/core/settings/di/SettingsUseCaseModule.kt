package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.GetGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.RefreshGlobalSettingsUseCase

/**
 * Internal use-case wiring for `core/settings`.
 *
 * Exposes thin facades over [GlobalSettingsRepository] for presentation and host layers.
 */
internal class SettingsUseCaseModule(
    globalSettingsRepository: GlobalSettingsRepository
) {
    /**
     * Forces a network refresh of global settings.
     */
    val refreshGlobalSettingsUseCase by lazy {
        RefreshGlobalSettingsUseCase(
            globalSettingsRepository
        )
    }

    /**
     * Returns cached settings when available, otherwise loads from storage or network.
     */
    val getGlobalSettingsUseCase by lazy {
        GetGlobalSettingsUseCase(
            globalSettingsRepository
        )
    }
}