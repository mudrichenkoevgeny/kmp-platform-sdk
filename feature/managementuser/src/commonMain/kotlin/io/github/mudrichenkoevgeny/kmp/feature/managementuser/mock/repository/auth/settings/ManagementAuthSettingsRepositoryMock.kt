package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Mock implementation of [ManagementAuthSettingsRepository] for testing.
 */
@InternalApi
public class ManagementAuthSettingsRepositoryMock : ManagementAuthSettingsRepository {

    private val authSettingsFlow = MutableStateFlow<ManagementAuthSettings?>(null)

    public var resultProvider: () -> AppResult<ManagementAuthSettings> = {
        authSettingsFlow.value?.let { AppResult.Success(it) }
            ?: AppResult.Error(
                CommonError.ContractViolation(
                    throwable = IllegalStateException(
                        "No mock settings provided. Call emit() or updateAuthSettings() first."
                    )
                )
            )
    }

    public var saveResultProvider: (ManagementAuthSettings) -> AppResult<ManagementAuthSettings> = {
        AppResult.Success(it)
    }

    public var resetResultProvider: () -> AppResult<ManagementAuthSettings> = { resultProvider() }

    public override suspend fun getManagementAuthSettings(): AppResult<ManagementAuthSettings> = resultProvider()

    public override suspend fun refreshManagementAuthSettings(): AppResult<ManagementAuthSettings> = resultProvider()

    public override suspend fun resetRemoteManagementAuthSettings(): AppResult<ManagementAuthSettings> {
        val result = resetResultProvider()
        if (result is AppResult.Success) {
            authSettingsFlow.value = result.data
        }
        return result
    }

    public override suspend fun saveRemoteManagementAuthSettings(
        authSettings: ManagementAuthSettings
    ): AppResult<ManagementAuthSettings> {
        val result = saveResultProvider(authSettings)
        if (result is AppResult.Success) {
            authSettingsFlow.value = result.data
        }
        return result
    }

    public override suspend fun updateManagementAuthSettings(authSettings: ManagementAuthSettings) {
        authSettingsFlow.value = authSettings
    }

    public override fun observeManagementAuthSettings(): Flow<ManagementAuthSettings?> = authSettingsFlow.asStateFlow()

    /** Emits a new [ManagementAuthSettings] value for subscribers. */
    public fun emit(authSettings: ManagementAuthSettings) {
        authSettingsFlow.value = authSettings
    }
}
