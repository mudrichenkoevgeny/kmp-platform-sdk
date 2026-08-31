package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.OpenAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.PublicAuthSettings

/**
 * Returns cached public auth settings when already loaded or stored; otherwise initiates a network load.
 *
 * @param openAuthSettingsRepository Remote auth settings API.
 */
class GetAuthSettingsUseCase(
    private val openAuthSettingsRepository: OpenAuthSettingsRepository
) {
    /**
     * @return [PublicAuthSettings] on success, or a mapped failure.
     */
    suspend operator fun invoke(): AppResult<PublicAuthSettings> {
        return openAuthSettingsRepository.getAuthSettings()
    }
}
