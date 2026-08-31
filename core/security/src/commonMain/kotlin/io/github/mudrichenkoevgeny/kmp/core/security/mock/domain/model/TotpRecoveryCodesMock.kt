package io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes

@InternalApi
fun totpRecoveryCodesMock(): TotpRecoveryCodes = TotpRecoveryCodes(
    codes = listOf("1234-5678", "8765-4321", "1111-2222", "3333-4444")
)
