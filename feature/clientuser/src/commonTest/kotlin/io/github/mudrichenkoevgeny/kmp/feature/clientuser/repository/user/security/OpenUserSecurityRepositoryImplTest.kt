package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.api.user.security.UserSecurityApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.user.UserStorageMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.totprecoverycodes.TotpRecoveryCodesPayload
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class OpenUserSecurityRepositoryImplTest {

    @Test
    fun `enableTotp should update storage and return recovery codes`() = runTest {
        val recoveryCodes = TotpRecoveryCodes(codes = listOf("code1"))
        val api = UserSecurityApiMock().apply {
            enableTotpResult = AppResult.Success(recoveryCodesPayload(recoveryCodes))
        }
        val storage = UserStorageMock().apply {
            updateCurrentUser(userDetailsMock().copy(isTotpEnabled = false))
        }
        val repository = OpenUserSecurityRepositoryImpl(api, storage)

        val result = repository.enableTotp("token", "code")

        assertIs<AppResult.Success<TotpRecoveryCodes>>(result)
        assertEquals(recoveryCodes.codes, result.data.codes)
        assertEquals(storage.getCurrentUser()?.isTotpEnabled, true)
    }

    @Test
    fun `disableTotp should update storage on success`() = runTest {
        val api = UserSecurityApiMock().apply {
            disableTotpResult = AppResult.Success(Unit)
        }
        val storage = UserStorageMock().apply {
            updateCurrentUser(userDetailsMock().copy(isTotpEnabled = true))
        }
        val repository = OpenUserSecurityRepositoryImpl(api, storage)

        val result = repository.disableTotp()

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(storage.getCurrentUser()?.isTotpEnabled, false)
    }

    private fun recoveryCodesPayload(codes: TotpRecoveryCodes) = TotpRecoveryCodesPayload(codes = codes.codes)
}
