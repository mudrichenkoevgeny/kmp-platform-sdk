package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration.RegistrationRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData

/**
 * Mock implementation of [RegistrationRepository] for testing.
 */
@InternalApi
public class RegistrationRepositoryMock : RegistrationRepository {

    public var lastEmail: String? = null
    public var lastPassword: String? = null
    public var lastConfirmationCode: String? = null

    public var authDataResultProvider: () -> AppResult<AuthData> = {
        AppResult.Error(
            CommonError.ContractViolation(
                throwable = IllegalStateException("No mock AuthData provided.")
            )
        )
    }

    public var otpConfirmationResultProvider: () -> AppResult<OtpConfirmation> = {
        AppResult.Error(
            CommonError.ContractViolation(
                throwable = IllegalStateException("No mock OtpConfirmation provided.")
            )
        )
    }

    public var remainingDelayProvider: (email: String) -> Int = { 0 }

    public override suspend fun registerByEmail(
        email: String,
        password: String,
        confirmationCode: String
    ): AppResult<AuthData> {
        lastEmail = email
        lastPassword = password
        lastConfirmationCode = confirmationCode
        return authDataResultProvider()
    }

    public override suspend fun sendRegistrationConfirmationToEmail(email: String): AppResult<OtpConfirmation> {
        lastEmail = email
        return otpConfirmationResultProvider()
    }

    public override fun getRemainingRegistrationConfirmationDelayInSeconds(email: String): Int =
        remainingDelayProvider(email)
}
