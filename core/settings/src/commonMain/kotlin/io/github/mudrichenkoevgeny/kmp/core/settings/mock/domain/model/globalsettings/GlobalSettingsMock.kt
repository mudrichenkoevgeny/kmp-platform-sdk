package io.github.mudrichenkoevgeny.kmp.core.settings.mock.domain.model.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.ManagementGlobalSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.model.globalsettings.OpenGlobalSettings

@InternalApi
fun openGlobalSettingsMock(
    privacy: String? = "https://privacy",
    terms: String? = "https://terms",
    email: String? = "a@b.c",
    maintenanceUntilEpochMillis: Long? = null,
    minSupportedAppVersions: Map<ClientType, String> = mapOf(
        ClientType.ANDROID to "1.0.0",
        ClientType.IOS to "1.0.0",
        ClientType.WEB to "1.0.0",
        ClientType.DESKTOP to "1.0.0"
    )
) = OpenGlobalSettings(
    privacyPolicyUrl = privacy,
    termsOfServiceUrl = terms,
    contactSupportEmail = email,
    maintenanceUntilEpochMillis = maintenanceUntilEpochMillis,
    minSupportedAppVersions = minSupportedAppVersions
)

@InternalApi
fun managementGlobalSettingsMock(
    privacy: String? = "https://privacy",
    terms: String? = "https://terms",
    email: String? = "a@b.c",
    maintenanceUntilEpochMillis: Long? = null,
    minSupportedAppVersions: Map<ClientType, String> = mapOf(
        ClientType.ANDROID to "1.0.0",
        ClientType.IOS to "1.0.0",
        ClientType.WEB to "1.0.0",
        ClientType.DESKTOP to "1.0.0"
    ),
    isTracingEnabled: Boolean = true,
    isMetricsEnabled: Boolean = true,
    isVerboseLoggingEnabled: Boolean = false
) = ManagementGlobalSettings(
    privacyPolicyUrl = privacy,
    termsOfServiceUrl = terms,
    contactSupportEmail = email,
    maintenanceUntilEpochMillis = maintenanceUntilEpochMillis,
    minSupportedAppVersions = minSupportedAppVersions,
    isTracingEnabled = isTracingEnabled,
    isMetricsEnabled = isMetricsEnabled,
    isVerboseLoggingEnabled = isVerboseLoggingEnabled
)
