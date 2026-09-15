package io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.accountlockout

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.accountlockout.AccountLockoutPolicyPayload

@InternalApi
fun accountLockoutPolicyPayloadMock(
    maxFailedPasswordAttempts: Int = 5,
    maxFailedOtpAttempts: Int = 5,
    maxFailedTotpAttempts: Int = 5,
    failedAttemptsWindowSeconds: Int = 300,
    lockoutDurationSeconds: Int = 300,
    indefiniteLockoutThreshold: Int = 3,
    isSelfServiceUnlockEnabled: Boolean = true
) = AccountLockoutPolicyPayload(
    maxFailedPasswordAttempts = maxFailedPasswordAttempts,
    maxFailedOtpAttempts = maxFailedOtpAttempts,
    maxFailedTotpAttempts = maxFailedTotpAttempts,
    failedAttemptsWindowSeconds = failedAttemptsWindowSeconds,
    lockoutDurationSeconds = lockoutDurationSeconds,
    indefiniteLockoutThreshold = indefiniteLockoutThreshold,
    isSelfServiceUnlockEnabled = isSelfServiceUnlockEnabled
)
