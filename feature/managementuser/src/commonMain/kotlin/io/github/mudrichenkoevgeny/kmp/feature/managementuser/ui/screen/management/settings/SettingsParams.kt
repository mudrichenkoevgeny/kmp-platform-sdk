package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings

import kotlinx.serialization.Serializable

/**
 * Screen destination parameters for management settings screens.
 *
 * @param dialog Optional active dialog name (e.g. "reset-confirm").
 */
@Serializable
data class SettingsParams(
    val dialog: String? = null
)
