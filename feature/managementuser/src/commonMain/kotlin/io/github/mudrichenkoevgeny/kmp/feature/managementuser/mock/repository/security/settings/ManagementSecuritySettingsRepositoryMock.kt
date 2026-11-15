package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.security.settings.ManagementSecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.domain.model.securitysettings.createTestManagementSecuritySettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Mock implementation of [ManagementSecuritySettingsRepository] for testing.
 */
@InternalApi
public class ManagementSecuritySettingsRepositoryMock(
    initialSettings: ManagementSecuritySettings? = createTestManagementSecuritySettings()
) : ManagementSecuritySettingsRepository {

    private val settingsStateFlow = MutableStateFlow(initialSettings)

    public var getManagementSecuritySettingsResult: AppResult<ManagementSecuritySettings>? = null
    public var refreshManagementSecuritySettingsResult: AppResult<ManagementSecuritySettings>? = null
    public var saveRemoteSecuritySettingsResult: AppResult<ManagementSecuritySettings>? = null
    public var resetRemoteSecuritySettingsResult: AppResult<ManagementSecuritySettings>? = null

    public override suspend fun getManagementSecuritySettings(): AppResult<ManagementSecuritySettings> {
        val customResult = getManagementSecuritySettingsResult
        if (customResult != null) {
            return customResult
        }
        val current = settingsStateFlow.value
        return if (current != null) {
            AppResult.Success(current)
        } else {
            val fresh = createTestManagementSecuritySettings()
            settingsStateFlow.value = fresh
            AppResult.Success(fresh)
        }
    }

    public override suspend fun saveRemoteManagementSecuritySettings(
        securitySettings: ManagementSecuritySettings
    ): AppResult<ManagementSecuritySettings> {
        val customResult = saveRemoteSecuritySettingsResult
        if (customResult != null) {
            return customResult
        }
        settingsStateFlow.value = securitySettings
        return AppResult.Success(securitySettings)
    }

    public override suspend fun refreshManagementSecuritySettings(): AppResult<ManagementSecuritySettings> {
        val customResult = refreshManagementSecuritySettingsResult
        if (customResult != null) {
            return customResult
        }
        return getManagementSecuritySettings()
    }

    public override suspend fun updateManagementSecuritySettings(securitySettings: ManagementSecuritySettings) {
        settingsStateFlow.value = securitySettings
    }

    public override suspend fun resetRemoteManagementSecuritySettings(): AppResult<ManagementSecuritySettings> {
        val customResult = resetRemoteSecuritySettingsResult
        if (customResult != null) {
            return customResult
        }
        return getManagementSecuritySettings()
    }

    public override fun observeManagementSecuritySettings(): Flow<ManagementSecuritySettings?> =
        settingsStateFlow.asStateFlow()
}
