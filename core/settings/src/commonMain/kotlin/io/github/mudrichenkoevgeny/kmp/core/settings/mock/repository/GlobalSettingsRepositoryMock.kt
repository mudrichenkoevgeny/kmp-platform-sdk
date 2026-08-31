package io.github.mudrichenkoevgeny.kmp.core.settings.mock.repository

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.GlobalSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@InternalApi
class GlobalSettingsRepositoryMock : GlobalSettingsRepository {

    private val _settingsFlow = MutableStateFlow<GlobalSettings?>(null)

    var resultProvider: () -> AppResult<GlobalSettings> = {
        _settingsFlow.value?.let { AppResult.Success(it) }
            ?: AppResult.Error(CommonError.Unknown())
    }

    override suspend fun getGlobalSettings(): AppResult<GlobalSettings> {
        return resultProvider()
    }

    override suspend fun refreshGlobalSettings(): AppResult<GlobalSettings> {
        val result = resultProvider()
        if (result is AppResult.Success) {
            _settingsFlow.value = result.data
        }
        return result
    }

    override suspend fun updateGlobalSettings(globalSettings: GlobalSettings) {
        _settingsFlow.value = globalSettings
    }

    override fun observeGlobalSettings(): Flow<GlobalSettings?> {
        return _settingsFlow.asStateFlow()
    }

    fun emit(settings: GlobalSettings?) {
        _settingsFlow.value = settings
    }
}