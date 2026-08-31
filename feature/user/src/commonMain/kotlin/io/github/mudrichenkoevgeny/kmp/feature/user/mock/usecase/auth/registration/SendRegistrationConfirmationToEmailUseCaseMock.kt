package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.registration

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.registration.RegistrationRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.SendRegistrationConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation

@InternalApi
class SendRegistrationConfirmationToEmailUseCaseMock : SendRegistrationConfirmationToEmailUseCase(
    registrationRepository = RegistrationRepositoryMock()
) {
    var resultProvider: (String) -> AppResult<OtpConfirmation> = { _ ->
        AppResult.Error(CommonError.Unknown())
    }

    override suspend fun execute(email: String): AppResult<OtpConfirmation> =
        resultProvider(email)
}
