package io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.identifier.IdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.SendAddPhoneIdentifierConfirmationUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation

@InternalApi
class SendAddPhoneIdentifierConfirmationUseCaseMock : SendAddPhoneIdentifierConfirmationUseCase(
    identifierRepository = IdentifierRepositoryMock()
) {
    var resultProvider: (String) -> AppResult<OtpConfirmation> = {
        AppResult.Success(
            OtpConfirmation(
                retryAfterSeconds = 60,
                numberOfSymbols = 6,
                expirationSeconds = 300
            )
        )
    }
    var executeCalls = 0

    override suspend fun invoke(phoneNumber: String): AppResult<OtpConfirmation> {
        executeCalls++
        return resultProvider(phoneNumber)
    }
}
