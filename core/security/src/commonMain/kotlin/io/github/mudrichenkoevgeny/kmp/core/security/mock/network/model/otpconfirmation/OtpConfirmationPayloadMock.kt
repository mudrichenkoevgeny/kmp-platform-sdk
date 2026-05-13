package io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.otpconfirmation

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload

@InternalApi
fun otpConfirmationPayloadMock(
    retryAfterSeconds: Int = 60,
    numberOfSymbols: Int = 6,
    expirationSeconds: Int = 300
) = OtpConfirmationPayload(
    retryAfterSeconds = retryAfterSeconds,
    numberOfSymbols = numberOfSymbols,
    expirationSeconds = expirationSeconds
)