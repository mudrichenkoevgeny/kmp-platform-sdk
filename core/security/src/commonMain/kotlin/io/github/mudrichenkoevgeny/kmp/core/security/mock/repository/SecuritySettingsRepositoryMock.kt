package io.github.mudrichenkoevgeny.kmp.core.security.mock.repository

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.repository.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.SecuritySettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@InternalApi
class SecuritySettingsRepositoryMock : SecuritySettingsRepository {

    private val securitySettingsFlow = MutableStateFlow<SecuritySettings?>(null)

    var resultProvider: () -> AppResult<SecuritySettings> = {
        securitySettingsFlow.value?.let { AppResult.Success(it) }
            ?: AppResult.Error(
                CommonError.ContractViolation(
                    throwable = IllegalStateException("No mock settings provided.")
                )
            )
    }

    var passwordPolicyResultProvider: () -> AppResult<SecuritySettings> = {
        resultProvider()
    }

    override suspend fun getSecuritySettings(): AppResult<SecuritySettings> = passwordPolicyResultProvider()

    override suspend fun refreshSecuritySettings(): AppResult<SecuritySettings> = passwordPolicyResultProvider()

    override suspend fun updateSecuritySettings(securitySettings: SecuritySettings) {
        securitySettingsFlow.value = securitySettings
    }

    override fun observeSecuritySettings(): Flow<SecuritySettings?> = securitySettingsFlow.asStateFlow()

    fun emit(securitySettings: SecuritySettings) {
        securitySettingsFlow.value = securitySettings
    }
}