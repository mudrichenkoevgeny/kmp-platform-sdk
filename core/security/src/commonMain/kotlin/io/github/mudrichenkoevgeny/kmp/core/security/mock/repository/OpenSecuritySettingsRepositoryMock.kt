package io.github.mudrichenkoevgeny.kmp.core.security.mock.repository

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.openSecuritySettingsMock
import io.github.mudrichenkoevgeny.kmp.core.security.repository.OpenSecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.OpenSecuritySettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@InternalApi
class OpenSecuritySettingsRepositoryMock(
    initialSettings: OpenSecuritySettings? = openSecuritySettingsMock()
) : OpenSecuritySettingsRepository {

    private val _settings = MutableStateFlow(initialSettings)

    var getSecuritySettingsResult: AppResult<OpenSecuritySettings>? = null
    var refreshSecuritySettingsResult: AppResult<OpenSecuritySettings>? = null

    override suspend fun getOpenSecuritySettings(): AppResult<OpenSecuritySettings> {
        val customResult = getSecuritySettingsResult
        if (customResult != null) return customResult
        val current = _settings.value
        return if (current != null) {
            AppResult.Success(current)
        } else {
            val fresh = openSecuritySettingsMock()
            _settings.value = fresh
            AppResult.Success(fresh)
        }
    }

    override suspend fun refreshOpenSecuritySettings(): AppResult<OpenSecuritySettings> {
        val customResult = refreshSecuritySettingsResult
        if (customResult != null) return customResult
        val fresh = openSecuritySettingsMock()
        _settings.value = fresh
        return AppResult.Success(fresh)
    }

    override suspend fun updateOpenSecuritySettings(securitySettings: OpenSecuritySettings) {
        _settings.value = securitySettings
    }

    override fun observeOpenSecuritySettings(): Flow<OpenSecuritySettings?> = _settings.asStateFlow()
}
