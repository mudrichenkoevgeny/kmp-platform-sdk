package io.github.mudrichenkoevgeny.kmp.core.settings.mock.network.model.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.ManagementGlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.OpenGlobalSettingsPayload

@InternalApi
fun openGlobalSettingsPayloadMock(
    privacy: String? = "https://privacy",
    terms: String? = "https://terms",
    email: String? = "a@b.c",
    maintenanceUntilEpochMillis: Long? = null,
    minSupportedAppVersions: Map<String, String> = mapOf(
        ClientType.ANDROID.serialName to "1.0.0",
        ClientType.IOS.serialName to "1.0.0",
        ClientType.WEB.serialName to "1.0.0",
        ClientType.DESKTOP.serialName to "1.0.0"
    )
) = OpenGlobalSettingsPayload(
    privacyPolicyUrl = privacy,
    termsOfServiceUrl = terms,
    contactSupportEmail = email,
    maintenanceUntilEpochMillis = maintenanceUntilEpochMillis,
    minSupportedAppVersions = minSupportedAppVersions
)

@InternalApi
fun managementGlobalSettingsPayloadMock(
    privacy: String? = "https://privacy",
    terms: String? = "https://terms",
    email: String? = "a@b.c",
    maintenanceUntilEpochMillis: Long? = null,
    minSupportedAppVersions: Map<String, String> = mapOf(
        ClientType.ANDROID.serialName to "1.0.0",
        ClientType.IOS.serialName to "1.0.0",
        ClientType.WEB.serialName to "1.0.0",
        ClientType.DESKTOP.serialName to "1.0.0"
    ),
    isTracingEnabled: Boolean = true,
    isMetricsEnabled: Boolean = true,
    isVerboseLoggingEnabled: Boolean = false
) = ManagementGlobalSettingsPayload(
    privacyPolicyUrl = privacy,
    termsOfServiceUrl = terms,
    contactSupportEmail = email,
    maintenanceUntilEpochMillis = maintenanceUntilEpochMillis,
    minSupportedAppVersions = minSupportedAppVersions,
    isTracingEnabled = isTracingEnabled,
    isMetricsEnabled = isMetricsEnabled,
    isVerboseLoggingEnabled = isVerboseLoggingEnabled
)
