package io.github.mudrichenkoevgeny.kmp.core.storage.di

import io.github.mudrichenkoevgeny.kmp.core.storage.repository.settings.OpenStorageSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.GetOpenStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.RefreshOpenStorageSettingsUseCase

/**
 * Internal use-case wiring for `core/storage`.
 *
 * Exposes refresh and read entry points built on [OpenStorageSettingsRepository].
 */
internal class StorageUseCaseModule(
    openStorageSettingsRepository: OpenStorageSettingsRepository
) {
    /**
     * Forces a network refresh of storage settings.
     */
    val refreshOpenStorageSettingsUseCase by lazy {
        RefreshOpenStorageSettingsUseCase(
            openStorageSettingsRepository
        )
    }

    /**
     * Retrieves stored or remote open storage settings.
     */
    val getOpenStorageSettingsUseCase by lazy {
        GetOpenStorageSettingsUseCase(
            openStorageSettingsRepository
        )
    }
}
