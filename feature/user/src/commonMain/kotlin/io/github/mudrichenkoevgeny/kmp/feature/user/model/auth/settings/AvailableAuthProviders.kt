package io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import kotlinx.serialization.Serializable

/** Primary vs secondary provider lists as configured server-side (drives login UI ordering and emphasis). */
@Serializable
data class AvailableAuthProviders(
    val primary: List<UserAuthProvider>,
    val secondary: List<UserAuthProvider>
)