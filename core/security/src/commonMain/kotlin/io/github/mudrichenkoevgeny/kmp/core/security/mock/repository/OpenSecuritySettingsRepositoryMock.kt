package io.github.mudrichenkoevgeny.kmp.core.security.mock.repository

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.repository.OpenSecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.OpenSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.domain.model.securitysettings.createTestOpenSecuritySettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Mock implementation of [OpenSecuritySettingsRepository] for testing purposes.
 */
@InternalApi
public class OpenSecuritySettingsRepositoryMock(
    initialSettings: OpenSecuritySettings? = createTestOpenSecuritySettings()
) : OpenSecuritySettingsRepository {

    private val settingsStateFlow = MutableStateFlow(initialSettings)

    public var getSecuritySettingsResult: AppResult<OpenSecuritySettings>? = null
    public var refreshSecuritySettingsResult: AppResult<OpenSecuritySettings>? = null

    public override suspend fun getOpenSecuritySettings(): AppResult<OpenSecuritySettings> {
        val customResult = getSecuritySettingsResult
        if (customResult != null) {
            return customResult
        }
        val current = settingsStateFlow.value
        return if (current != null) {
            AppResult.Success(current)
        } else {
            val fresh = createTestOpenSecuritySettings()
            settingsStateFlow.value = fresh
            AppResult.Success(fresh)
        }
    }

    public override suspend fun refreshOpenSecuritySettings(): AppResult<OpenSecuritySettings> {
        val customResult = refreshSecuritySettingsResult
        if (customResult != null) {
            return customResult
        }
        val fresh = createTestOpenSecuritySettings()
        settingsStateFlow.value = fresh
        return AppResult.Success(fresh)
    }

    public override suspend fun updateOpenSecuritySettings(securitySettings: OpenSecuritySettings) {
        settingsStateFlow.value = securitySettings
    }

    public override fun observeOpenSecuritySettings(): Flow<OpenSecuritySettings?> = settingsStateFlow.asStateFlow()
}
