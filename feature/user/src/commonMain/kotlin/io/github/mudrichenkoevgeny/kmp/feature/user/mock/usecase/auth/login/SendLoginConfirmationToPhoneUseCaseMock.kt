package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.login.LoginRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.SendLoginConfirmationToPhoneUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation

@InternalApi
class SendLoginConfirmationToPhoneUseCaseMock : SendLoginConfirmationToPhoneUseCase(
    loginRepository = LoginRepositoryMock()
) {
    var resultProvider: (String) -> AppResult<OtpConfirmation> = { _ ->
        AppResult.Error(CommonError.Unknown())
    }

    override suspend fun execute(phoneNumber: String): AppResult<OtpConfirmation> =
        resultProvider(phoneNumber)
}
