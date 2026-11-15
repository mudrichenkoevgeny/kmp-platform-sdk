package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.resetpassword

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.resetpassword.ResetPasswordRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.SendResetPasswordConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation

/**
 * Mock implementation of [SendResetPasswordConfirmationToEmailUseCase] for testing.
 */
@InternalApi
public class SendResetPasswordConfirmationToEmailUseCaseMock : SendResetPasswordConfirmationToEmailUseCase(
    resetPasswordRepository = ResetPasswordRepositoryMock()
) {
    public var resultProvider: (String) -> AppResult<OtpConfirmation> = { _ ->
        AppResult.Error(CommonError.Unknown())
    }

    public override suspend fun execute(email: String): AppResult<OtpConfirmation> =
        resultProvider(email)
}
