package io.github.mudrichenkoevgeny.kmp.core.settings.usecase

import io.github.mudrichenkoevgeny.kmp.core.settings.repository.OpenGlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.OpenGlobalSettings
import kotlinx.coroutines.flow.Flow

/**
 * Use case that delegates to [OpenGlobalSettingsRepository.observeOpenGlobalSettings].
 *
 * @param openGlobalSettingsRepository Source of truth for cached or network-backed settings.
 */
class ObserveOpenGlobalSettingsUseCase(
    private val openGlobalSettingsRepository: OpenGlobalSettingsRepository
) {
    /**
     * @return Cold [Flow] emitting the latest [OpenGlobalSettings] or `null`.
     */
    operator fun invoke(): Flow<OpenGlobalSettings?> {
        return openGlobalSettingsRepository.observeOpenGlobalSettings()
    }
}
