package io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.unlock

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.auth.unlock.SelfManagementUnlockApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.confirmation.ConfirmationRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.mapper.otpconfirmation.toOtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.unlock.UnlockByEmailConfirmationRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.confirmation.SendConfirmationToEmailRequest
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.network.model.otpconfirmation.createTestOtpConfirmationPayload
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class SelfManagementUnlockRepositoryImplTest {

    private val api = SelfManagementUnlockApiMock()
    private val confirmation = ConfirmationRepositoryMock()
    private val repo = SelfManagementUnlockRepositoryImpl(api, confirmation)

    @Test
    fun sendUnlockEmailConfirmation_forwardsRequestAndMapsResponse() = runTest {
        val wire = createTestOtpConfirmationPayload()
        api.sendUnlockEmailConfirmationResult = AppResult.Success(wire)

        val result = repo.sendUnlockEmailConfirmation(UNLOCK_EMAIL)

        val success = assertIs<AppResult.Success<*>>(result)
        assertEquals(wire.toOtpConfirmation(), success.data)
        assertEquals(
            SendConfirmationToEmailRequest(UNLOCK_EMAIL),
            api.lastSendUnlockEmailConfirmationRequest
        )
    }

    @Test
    fun unlockByEmail_forwardsRequestToApi() = runTest {
        api.unlockByEmailResult = AppResult.Success(Unit)

        val result = repo.unlockByEmail(UNLOCK_EMAIL, CONFIRMATION_CODE)

        assertIs<AppResult.Success<Unit>>(result)
        assertEquals(
            UnlockByEmailConfirmationRequest(email = UNLOCK_EMAIL, confirmationCode = CONFIRMATION_CODE),
            api.lastUnlockByEmailRequest
        )
    }

    @Test
    fun sendUnlockPhoneConfirmation_returnsContractViolationError() = runTest {
        val result = repo.sendUnlockPhoneConfirmation("+123456789")

        val error = assertIs<AppResult.Error>(result)
        assertIs<CommonError.ContractViolation>(error.error)
    }

    @Test
    fun unlockByPhone_returnsContractViolationError() = runTest {
        val result = repo.unlockByPhone("+123456789", CONFIRMATION_CODE)

        val error = assertIs<AppResult.Error>(result)
        assertIs<CommonError.ContractViolation>(error.error)
    }

    @Test
    fun unlockByExternalAuthProvider_returnsContractViolationError() = runTest {
        val result = repo.unlockByExternalAuthProvider("google", "token123")

        val error = assertIs<AppResult.Error>(result)
        assertIs<CommonError.ContractViolation>(error.error)
    }

    private companion object {
        private const val UNLOCK_EMAIL = "admin@example.com"
        private const val CONFIRMATION_CODE = "123456"
    }
}
