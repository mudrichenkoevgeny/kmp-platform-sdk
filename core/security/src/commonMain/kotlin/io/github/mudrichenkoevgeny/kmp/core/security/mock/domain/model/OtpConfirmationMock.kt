package io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation

@InternalApi
fun otpConfirmationMock(
    retryAfterSeconds: Int = 60,
    numberOfSymbols: Int = 6,
    expirationSeconds: Int = 300
) = OtpConfirmation(
    retryAfterSeconds = retryAfterSeconds,
    numberOfSymbols = numberOfSymbols,
    expirationSeconds = expirationSeconds
)