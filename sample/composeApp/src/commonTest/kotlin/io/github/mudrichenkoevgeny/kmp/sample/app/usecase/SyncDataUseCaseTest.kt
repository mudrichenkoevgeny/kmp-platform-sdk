package io.github.mudrichenkoevgeny.kmp.sample.app.usecase

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.RefreshSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.usecase.RefreshGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.AuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.RefreshAuthSettingsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.model.PasswordPolicy
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private class CountingGlobalSettingsRepository : GlobalSettingsRepository {
    var refreshCalls = 0

    override suspend fun getGlobalSettings(): AppResult<GlobalSettings> =
        AppResult.Error(CommonError.Unknown())

    override suspend fun refreshGlobalSettings(): AppResult<GlobalSettings> {
        refreshCalls++
        return AppResult.Success(GlobalSettings(null, null, null))
    }

    override suspend fun updateGlobalSettings(globalSettings: GlobalSettings) {}

    override fun observeGlobalSettings() = flowOf<GlobalSettings?>(null)
}

private class CountingSecuritySettingsRepository : SecuritySettingsRepository {
    var refreshCalls = 0

    override suspend fun getSecuritySettings(): AppResult<SecuritySettings> =
        AppResult.Error(CommonError.Unknown())

    override suspend fun refreshSecuritySettings(): AppResult<SecuritySettings> {
        refreshCalls++
        return AppResult.Success(SecuritySettings(passwordPolicy = PasswordPolicy()))
    }

    override suspend fun updateSecuritySettings(securitySettings: SecuritySettings) {}

    override fun observeSecuritySettings() = flowOf<SecuritySettings?>(null)
}

private class CountingAuthSettingsRepository : AuthSettingsRepository {
    var refreshCalls = 0

    override suspend fun getAuthSettings(): AppResult<AuthSettings> =
        AppResult.Error(CommonError.Unknown())

    override suspend fun refreshAuthSettings(): AppResult<AuthSettings> {
        refreshCalls++
        val settings = AuthSettings(AvailableAuthProviders(primary = emptyList(), secondary = emptyList()))
        return AppResult.Success(settings)
    }

    override suspend fun updateAuthSettings(authSettings: AuthSettings) {}

    override fun observeAuthSettings() = flowOf<AuthSettings?>(null)
}

class SyncDataUseCaseTest {

    @Test
    fun `invoke runs all three refresh use cases`() = runTest {
        val globalRepo = CountingGlobalSettingsRepository()
        val securityRepo = CountingSecuritySettingsRepository()
        val authRepo = CountingAuthSettingsRepository()

        val useCase = SyncDataUseCase(
            refreshGlobalSettingsUseCase = RefreshGlobalSettingsUseCase(globalRepo),
            refreshSecuritySettingsUseCase = RefreshSecuritySettingsUseCase(securityRepo),
            refreshAuthSettingsUseCase = RefreshAuthSettingsUseCase(authRepo)
        )

        useCase()

        assertEquals(1, globalRepo.refreshCalls)
        assertEquals(1, securityRepo.refreshCalls)
        assertEquals(1, authRepo.refreshCalls)
    }
}
