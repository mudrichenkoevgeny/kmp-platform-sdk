package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.OpenAuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.OpenAuthSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Mock implementation of [OpenAuthSettingsRepository] for testing.
 */
@InternalApi
public class OpenAuthSettingsRepositoryMock : OpenAuthSettingsRepository {

    private val authSettingsFlow = MutableStateFlow<OpenAuthSettings?>(null)

    public var resultProvider: () -> AppResult<OpenAuthSettings> = {
        authSettingsFlow.value?.let { AppResult.Success(it) }
            ?: AppResult.Error(
                CommonError.ContractViolation(
                    throwable = IllegalStateException(
                        "No mock settings provided. Call emit() or updateOpenAuthSettings() first."
                    )
                )
            )
    }

    public override suspend fun getOpenAuthSettings(): AppResult<OpenAuthSettings> = resultProvider()

    public override suspend fun refreshOpenAuthSettings(): AppResult<OpenAuthSettings> = resultProvider()

    public override suspend fun updateOpenAuthSettings(authSettings: OpenAuthSettings) {
        authSettingsFlow.value = authSettings
    }

    public override fun observeOpenAuthSettings(): Flow<OpenAuthSettings?> = authSettingsFlow.asStateFlow()

    /**
     * Emits open auth settings to subscribers.
     *
     * @param authSettings The settings to emit.
     */
    public fun emit(authSettings: OpenAuthSettings) {
        authSettingsFlow.value = authSettings
    }
}
