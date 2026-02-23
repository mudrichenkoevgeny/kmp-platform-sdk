package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import kotlinx.coroutines.flow.Flow

interface AuthSettingsRepository {
    suspend fun getAuthSettings(): AppResult<AuthSettings>
    suspend fun refreshAuthSettings(): AppResult<AuthSettings>
    suspend fun updateAuthSettings(authSettings: AuthSettings)
    fun observeAuthSettings(): Flow<AuthSettings?>
}