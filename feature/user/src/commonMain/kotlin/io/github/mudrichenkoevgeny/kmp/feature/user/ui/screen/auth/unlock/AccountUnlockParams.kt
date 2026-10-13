package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock

import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
import kotlinx.serialization.Serializable

/**
 * Navigation arguments for account unlock flow.
 */
@Serializable
data class AccountUnlockParams(
    val lockoutType: AccountLockoutType? = null,
    val lockoutUntil: Long? = null
)
