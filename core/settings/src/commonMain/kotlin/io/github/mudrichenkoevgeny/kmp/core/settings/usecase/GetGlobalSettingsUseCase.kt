package io.github.mudrichenkoevgeny.kmp.core.settings.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings.GlobalSettingsRepository

class GetGlobalSettingsUseCase(
    private val globalSettingsRepository: GlobalSettingsRepository
) {
    suspend operator fun invoke(): AppResult<GlobalSettings> {
        return globalSettingsRepository.getGlobalSettings()
    }
}