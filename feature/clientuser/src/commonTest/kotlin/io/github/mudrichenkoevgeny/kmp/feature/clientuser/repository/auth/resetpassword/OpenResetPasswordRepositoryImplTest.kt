package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.api.auth.resetpassword.ResetPasswordApiMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.identifier.userIdentifierPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.confirmation.ConfirmationRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.identifier.toUserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.identifier.UserIdentifierPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.ResetPasswordRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.password.SendResetPasswordConfirmationRequest
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class OpenResetPasswordRepositoryImplTest {

    @Test
    fun `resetPassword should forward request and map response`() = runTest {
        val payload = userIdentifierPayloadMock()
        val api = ResetPasswordApiMock().apply {
            resetPasswordResult = AppResult.Success(payload)
        }
        val repository = OpenResetPasswordRepositoryImpl(api, ConfirmationRepositoryMock())

        val result = repository.resetPassword("email", "new", "code")

        assertIs<AppResult.Success<UserIdentifier>>(result)
        assertEquals(payload.toUserIdentifier().identifier, result.data.identifier)
    }
}
