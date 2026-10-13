package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target

import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod
import kotlinx.serialization.Serializable

/**
 * Navigation arguments for unlock target input screen.
 */
@Serializable
data class UnlockTargetInputParams(
    val method: UnlockMethod
)
