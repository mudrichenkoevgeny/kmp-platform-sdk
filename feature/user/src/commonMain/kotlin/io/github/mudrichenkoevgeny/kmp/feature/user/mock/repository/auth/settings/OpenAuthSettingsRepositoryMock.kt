package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.OpenAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.PublicAuthSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@InternalApi
class OpenAuthSettingsRepositoryMock : OpenAuthSettingsRepository {

    private val authSettingsFlow = MutableStateFlow<PublicAuthSettings?>(null)

    var resultProvider: () -> AppResult<PublicAuthSettings> = {
        authSettingsFlow.value?.let { AppResult.Success(it) }
            ?: AppResult.Error(
                CommonError.ContractViolation(
                    throwable = IllegalStateException("No mock settings provided. Call emit() or updateAuthSettings() first.")
                )
            )
    }

    override suspend fun getAuthSettings(): AppResult<PublicAuthSettings> = resultProvider()

    override suspend fun refreshAuthSettings(): AppResult<PublicAuthSettings> = resultProvider()

    override suspend fun updateAuthSettings(authSettings: PublicAuthSettings) {
        authSettingsFlow.value = authSettings
    }

    override fun observeAuthSettings(): Flow<PublicAuthSettings?> = authSettingsFlow.asStateFlow()

    fun emit(authSettings: PublicAuthSettings) {
        authSettingsFlow.value = authSettings
    }
}
