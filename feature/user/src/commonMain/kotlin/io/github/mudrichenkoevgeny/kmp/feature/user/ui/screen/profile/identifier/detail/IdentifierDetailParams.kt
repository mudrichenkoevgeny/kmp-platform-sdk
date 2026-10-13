package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail

import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.toUserIdentifierIdOrThrow
import kotlinx.serialization.Serializable

/**
 * Navigation arguments for identifier detail screen.
 */
@Serializable
data class IdentifierDetailParams(
    val identifierIdValue: String
) {
    val identifierId: UserIdentifierId get() = identifierIdValue.toUserIdentifierIdOrThrow()
}
