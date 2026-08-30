package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.user.security.SelfManagementUserSecurityApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.totprecoverycodes.toTotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.totpsetup.toTotpSetup
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totprecoverycodes.TotpRecoveryCodesPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totpsetup.TotpSetupPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.accountstatus.UserAccountStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.role.UserRole
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserDetails
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Instant

@InternalApi
class SelfManagementUserSecurityRepositoryImplTest {

    private val api = SelfManagementUserSecurityApiMock()
    private val userStorage = UserStorageMock()
    private val repository = SelfManagementUserSecurityRepositoryImpl(api, userStorage)

    private val initialUser = UserDetails(
        role = UserRole.USER,
        accountStatus = UserAccountStatus.ACTIVE,
        accountStatusBeforeDeletion = null,
        authorityLevel = 1,
        permissionCodes = emptySet(),
        isTotpEnabled = false,
        createdAt = Instant.DISTANT_PAST
    )

    @Test
    fun `setupTotp returns mapped result`() = runTest {
        val payload = TotpSetupPayload(secretKey = "secret", otpAuthUrl = "url", mfaToken = "token")
        api.setupTotpResult = AppResult.Success(payload)

        val result = repository.setupTotp()

        assertIs<AppResult.Success<*>>(result)
        assertEquals(payload.toTotpSetup(), (result as AppResult.Success).data)
    }

    @Test
    fun `enableTotp updates storage and returns codes`() = runTest {
        userStorage.updateCurrentUser(initialUser)
        val payload = TotpRecoveryCodesPayload(codes = listOf("123"))
        api.enableTotpResult = AppResult.Success(payload)

        val result = repository.enableTotp("token", "code")

        assertIs<AppResult.Success<*>>(result)
        assertEquals(payload.toTotpRecoveryCodes(), (result as AppResult.Success).data)
        assertEquals(true, userStorage.getCurrentUser()?.isTotpEnabled)
    }

    @Test
    fun `disableTotp updates storage and returns success`() = runTest {
        userStorage.updateCurrentUser(initialUser.copy(isTotpEnabled = true))
        api.disableTotpResult = AppResult.Success(Unit)

        val result = repository.disableTotp()

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(false, userStorage.getCurrentUser()?.isTotpEnabled)
    }

    @Test
    fun `getRecoveryCodes returns mapped result`() = runTest {
        val payload = TotpRecoveryCodesPayload(codes = listOf("code1"))
        api.getRecoveryCodesResult = AppResult.Success(payload)

        val result = repository.getRecoveryCodes()

        assertIs<AppResult.Success<*>>(result)
        assertEquals(payload.toTotpRecoveryCodes(), (result as AppResult.Success).data)
    }

    @Test
    fun `regenerateRecoveryCodes returns mapped result`() = runTest {
        val payload = TotpRecoveryCodesPayload(codes = listOf("new-code"))
        api.regenerateRecoveryCodesResult = AppResult.Success(payload)

        val result = repository.regenerateRecoveryCodes()

        assertIs<AppResult.Success<*>>(result)
        assertEquals(payload.toTotpRecoveryCodes(), (result as AppResult.Success).data)
    }
}