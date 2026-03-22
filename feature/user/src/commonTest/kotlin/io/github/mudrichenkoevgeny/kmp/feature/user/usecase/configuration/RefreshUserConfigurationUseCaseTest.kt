package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.model.securitysettings.SecuritySettings
import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings
import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.configuration.toUserConfiguration
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.configuration.UserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.AuthSettingsRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.configuration.UserConfigurationResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class RefreshUserConfigurationUseCaseTest {

    @Test
    fun invoke_persistsSlices_whenApiSucceeds() = runTest {
        val wire = wireUserConfigurationResponse()
        val expected = wire.toUserConfiguration()
        val api = FakeUserConfigurationApi().apply { result = AppResult.Success(wire) }
        val global = FakeGlobalSettingsRepository()
        val security = FakeSecuritySettingsRepository()
        val auth = FakeAuthSettingsRepository()
        val useCase = RefreshUserConfigurationUseCase(api, global, security, auth)

        val invokeResult = useCase()

        assertIs<AppResult.Success<*>>(invokeResult)
        assertEquals(expected.globalSettings, global.lastUpdate)
        assertEquals(expected.securitySettings, security.lastUpdate)
        assertEquals(expected.authSettings, auth.lastUpdate)
    }

    @Test
    fun invoke_doesNotPersist_whenApiFails() = runTest {
        val err = CommonError.Unknown(isRetryable = NOT_RETRYABLE)
        val api = FakeUserConfigurationApi().apply { result = AppResult.Error(err) }
        val global = FakeGlobalSettingsRepository()
        val security = FakeSecuritySettingsRepository()
        val auth = FakeAuthSettingsRepository()
        val useCase = RefreshUserConfigurationUseCase(api, global, security, auth)

        val invokeResult = useCase()

        assertIs<AppResult.Error>(invokeResult)
        assertNull(global.lastUpdate)
        assertNull(security.lastUpdate)
        assertNull(auth.lastUpdate)
    }

    private class FakeUserConfigurationApi : UserConfigurationApi {
        var result: AppResult<UserConfigurationResponse> = AppResult.Error(CommonError.Unknown(isRetryable = NOT_RETRYABLE))

        override suspend fun getUserConfiguration(): AppResult<UserConfigurationResponse> = result
    }

    private class FakeGlobalSettingsRepository : GlobalSettingsRepository {
        var lastUpdate: GlobalSettings? = null
        private val flow = MutableStateFlow<GlobalSettings?>(null)

        override suspend fun getGlobalSettings(): AppResult<GlobalSettings> = error(STUB_NOT_USED)

        override suspend fun refreshGlobalSettings(): AppResult<GlobalSettings> = error(STUB_NOT_USED)

        override suspend fun updateGlobalSettings(globalSettings: GlobalSettings) {
            lastUpdate = globalSettings
        }

        override fun observeGlobalSettings(): Flow<GlobalSettings?> = flow.asStateFlow()
    }

    private class FakeSecuritySettingsRepository : SecuritySettingsRepository {
        var lastUpdate: SecuritySettings? = null
        private val flow = MutableStateFlow<SecuritySettings?>(null)

        override suspend fun getSecuritySettings(): AppResult<SecuritySettings> = error(STUB_NOT_USED)

        override suspend fun refreshSecuritySettings(): AppResult<SecuritySettings> = error(STUB_NOT_USED)

        override suspend fun updateSecuritySettings(securitySettings: SecuritySettings) {
            lastUpdate = securitySettings
        }

        override fun observeSecuritySettings(): Flow<SecuritySettings?> = flow.asStateFlow()
    }

    private class FakeAuthSettingsRepository : AuthSettingsRepository {
        var lastUpdate: AuthSettings? = null
        private val flow = MutableStateFlow<AuthSettings?>(null)

        override suspend fun getAuthSettings(): AppResult<AuthSettings> = error(STUB_NOT_USED)

        override suspend fun refreshAuthSettings(): AppResult<AuthSettings> = error(STUB_NOT_USED)

        override suspend fun updateAuthSettings(authSettings: AuthSettings) {
            lastUpdate = authSettings
        }

        override fun observeAuthSettings(): Flow<AuthSettings?> = flow.asStateFlow()
    }

    private companion object {
        private const val NOT_RETRYABLE = false
        private const val STUB_NOT_USED = "not used"
    }
}
