package io.github.mudrichenkoevgeny.kmp.core.settings.mapper.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.settings.model.globalsettings.GlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.response.GlobalSettingsResponse

/**
 * Maps a wire [GlobalSettingsResponse] from the foundation module into the SDK [GlobalSettings] model.
 *
 * @return Domain [GlobalSettings] with the same nullable fields as the response.
 */
fun GlobalSettingsResponse.toGlobalSettings() = GlobalSettings(
    privacyPolicyUrl = this.privacyPolicyUrl,
    termsOfServiceUrl = this.termsOfServiceUrl,
    contactSupportEmail = this.contactSupportEmail
)