package io.github.mudrichenkoevgeny.kmp.core.settings.mapper.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.response.GlobalSettingsResponse

fun GlobalSettingsResponse.toGlobalSettings() = GlobalSettings(
    privacyPolicyUrl = this.privacyPolicyUrl,
    termsOfServiceUrl = this.termsOfServiceUrl,
    contactSupportEmail = this.contactSupportEmail
)