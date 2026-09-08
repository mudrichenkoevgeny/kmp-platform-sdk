package io.github.mudrichenkoevgeny.kmp.core.settings.di

import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.RefreshOpenGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.OpenGlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.GetOpenGlobalSettingsUseCase

/**
 * Internal use-case wiring for `core/settings`.
 *
 * Exposes thin facades over [OpenGlobalSettingsRepository] for presentation and host layers.
 */
internal class SettingsUseCaseModule(
    openGlobalSettingsRepository: OpenGlobalSettingsRepository
) {
    /**
     * Forces a network refresh of global settings.
     */
    val refreshOpenGlobalSettingsUseCase by lazy {
        RefreshOpenGlobalSettingsUseCase(
            openGlobalSettingsRepository
        )
    }

    /**
     * Returns cached settings when available, otherwise loads from storage or network.
     */
    val getOpenGlobalSettingsUseCase by lazy {
        GetOpenGlobalSettingsUseCase(
            openGlobalSettingsRepository
        )
    }
}