package io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup

@InternalApi
fun totpSetupMock(): TotpSetup = TotpSetup(
    secretKey = "JBSWY3DPEHPK3PXP",
    otpAuthUrl = "otpauth://totp/Example:alice@google.com?secret=JBSWY3DPEHPK3PXP&issuer=Example",
    mfaToken = "temporary_mfa_token"
)
