package io.github.mudrichenkoevgeny.kmp.core.settings.mock.domain.model.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.GlobalSettings

@InternalApi
fun globalSettingsMock(
    privacy: String? = "https://privacy",
    terms: String? = "https://terms",
    email: String? = "a@b.c"
) = GlobalSettings(
    privacyPolicyUrl = privacy,
    termsOfServiceUrl = terms,
    contactSupportEmail = email
)