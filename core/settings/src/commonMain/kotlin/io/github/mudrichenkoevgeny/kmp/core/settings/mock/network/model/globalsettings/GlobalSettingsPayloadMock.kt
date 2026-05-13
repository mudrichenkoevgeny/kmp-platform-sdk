package io.github.mudrichenkoevgeny.kmp.core.settings.mock.network.model.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.GlobalSettingsPayload

@InternalApi
fun globalSettingsPayloadMock(
    privacy: String? = "https://privacy",
    terms: String? = "https://terms",
    email: String? = "a@b.c"
) = GlobalSettingsPayload(
    privacyPolicyUrl = privacy,
    termsOfServiceUrl = terms,
    contactSupportEmail = email
)