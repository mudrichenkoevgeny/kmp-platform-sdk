package io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings

import kotlinx.serialization.Serializable

/**
 * Serializable snapshot of server-provided global URLs and contact fields exposed to the app.
 *
 * @param privacyPolicyUrl Optional privacy policy URL.
 * @param termsOfServiceUrl Optional terms of service URL.
 * @param contactSupportEmail Optional support contact address.
 */
@Serializable
data class GlobalSettings(
    val privacyPolicyUrl: String?,
    val termsOfServiceUrl: String?,
    val contactSupportEmail: String?
)
