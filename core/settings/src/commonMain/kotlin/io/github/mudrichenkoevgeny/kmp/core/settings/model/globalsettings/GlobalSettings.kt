package io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings

import kotlinx.serialization.Serializable

@Serializable
data class GlobalSettings(
    val privacyPolicyUrl: String?,
    val termsOfServiceUrl: String?,
    val contactSupportEmail: String?
)
