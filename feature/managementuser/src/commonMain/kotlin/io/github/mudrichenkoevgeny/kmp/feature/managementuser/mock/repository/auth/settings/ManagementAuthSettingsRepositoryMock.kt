package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@InternalApi
class ManagementAuthSettingsRepositoryMock : ManagementAuthSettingsRepository {

    private val authSettingsFlow = MutableStateFlow<ManagementAuthSettings?>(null)

    var resultProvider: () -> AppResult<ManagementAuthSettings> = {
        authSettingsFlow.value?.let { AppResult.Success(it) }
            ?: AppResult.Error(
                CommonError.ContractViolation(
                    throwable = IllegalStateException("No mock settings provided. Call emit() or updateAuthSettings() first.")
                )
            )
    }

    var saveResultProvider: () -> AppResult<Unit> = {
        AppResult.Success(Unit)
    }

    override suspend fun getManagementAuthSettings(): AppResult<ManagementAuthSettings> = resultProvider()

    override suspend fun refreshManagementAuthSettings(): AppResult<ManagementAuthSettings> = resultProvider()

    override suspend fun saveRemoteAuthSettings(authSettings: ManagementAuthSettings): AppResult<Unit> {
        val result = saveResultProvider()
        if (result is AppResult.Success) {
            authSettingsFlow.value = authSettings
        }
        return result
    }

    override suspend fun updateManagementAuthSettings(authSettings: ManagementAuthSettings) {
        authSettingsFlow.value = authSettings
    }

    override fun observeManagementAuthSettings(): Flow<ManagementAuthSettings?> = authSettingsFlow.asStateFlow()

    fun emit(authSettings: ManagementAuthSettings) {
        authSettingsFlow.value = authSettings
    }
}