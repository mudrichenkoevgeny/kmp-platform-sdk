package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.managementSecuritySettingsMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.security.settings.ManagementSecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@InternalApi
class ManagementSecuritySettingsRepositoryMock(
    initialSettings: ManagementSecuritySettings? = managementSecuritySettingsMock()
) : ManagementSecuritySettingsRepository {

    private val _settings = MutableStateFlow(initialSettings)

    var getManagementSecuritySettingsResult: AppResult<ManagementSecuritySettings>? = null
    var refreshManagementSecuritySettingsResult: AppResult<ManagementSecuritySettings>? = null
    var saveRemoteSecuritySettingsResult: AppResult<Unit>? = null

    override suspend fun getManagementSecuritySettings(): AppResult<ManagementSecuritySettings> {
        val customResult = getManagementSecuritySettingsResult
        if (customResult != null) return customResult
        val current = _settings.value
        return if (current != null) {
            AppResult.Success(current)
        } else {
            val fresh = managementSecuritySettingsMock()
            _settings.value = fresh
            AppResult.Success(fresh)
        }
    }

    override suspend fun saveRemoteManagementSecuritySettings(securitySettings: ManagementSecuritySettings): AppResult<Unit> {
        val customResult = saveRemoteSecuritySettingsResult
        if (customResult != null) return customResult
        _settings.value = securitySettings
        return AppResult.Success(Unit)
    }

    override suspend fun refreshManagementSecuritySettings(): AppResult<ManagementSecuritySettings> {
        val customResult = refreshManagementSecuritySettingsResult
        if (customResult != null) return customResult
        return getManagementSecuritySettings()
    }

    override suspend fun updateManagementSecuritySettings(securitySettings: ManagementSecuritySettings) {
        _settings.value = securitySettings
    }

    override fun observeManagementSecuritySettings(): Flow<ManagementSecuritySettings?> = _settings.asStateFlow()
}
