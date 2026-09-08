package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.OpenAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.OpenAuthSettings
import kotlinx.coroutines.flow.Flow

/**
 * Observes the in-memory public auth settings snapshot.
 *
 * @param openAuthSettingsRepository Remote auth settings API.
 */
class ObserveAuthSettingsUseCase(
    private val openAuthSettingsRepository: OpenAuthSettingsRepository
) {
    /**
     * @return [Flow] of the current [OpenAuthSettings] or `null`.
     */
    operator fun invoke(): Flow<OpenAuthSettings?> {
        return openAuthSettingsRepository.observeOpenAuthSettings()
    }
}
