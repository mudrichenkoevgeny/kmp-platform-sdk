package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list

import kotlinx.serialization.Serializable

/**
 * Screen destination parameters for the self identifier list screen.
 *
 * @param dialog Optional active dialog name (e.g. "add").
 * @param step Optional active dialog step (e.g. "email", "phone").
 * @param value Pre-filled email or phone string parameter.
 */
@Serializable
data class IdentifierListParams(
    val dialog: String? = null,
    val step: String? = null,
    val value: String? = null
)
