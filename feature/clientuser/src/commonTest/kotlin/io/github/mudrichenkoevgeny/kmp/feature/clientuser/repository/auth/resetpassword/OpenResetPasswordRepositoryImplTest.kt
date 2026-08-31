package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.identifier.userIdentifierPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.resetpassword.ResetPasswordApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class OpenResetPasswordRepositoryImplTest {

    @Test
    fun `resetPassword should forward request and map response`() = runTest {
        val payload = userIdentifierPayloadMock()
        val api = FakeResetPasswordApi(resetResult = AppResult.Success(payload))
        val repository = OpenResetPasswordRepositoryImpl(api, FakeConfirmationRepository())

        val result = repository.resetPassword("email", "new", "code")

        assertIs<AppResult.Success<UserIdentifier>>(result)
        assertEquals(payload.toUserIdentifier().identifier, result.data.identifier)
    }

    private class FakeResetPasswordApi(
        private val resetResult: AppResult<UserIdentifierPayload> = AppResult.Error(io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError.Unknown())
    ) : ResetPasswordApi {
        override suspend fun resetPassword(request: io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.ResetPasswordRequest): AppResult<UserIdentifierPayload> = resetResult
        override suspend fun sendResetPasswordConfirmationToEmail(request: io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.SendResetPasswordConfirmationRequest): AppResult<io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload> = error("N/A")
    }

    private class FakeConfirmationRepository : ConfirmationRepository {
        override suspend fun <T> executeWithTimer(type: io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType, identifier: String, action: suspend () -> AppResult<T>): AppResult<T> = action()
        override fun getRemainingDelay(type: io.github.mudrichenkoevgeny.kmp.feature.user.model.confirmation.ConfirmationType, identifier: String): Int = 0
    }
}
