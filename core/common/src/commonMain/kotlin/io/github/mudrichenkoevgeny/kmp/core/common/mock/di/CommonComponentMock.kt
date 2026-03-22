package io.github.mudrichenkoevgeny.kmp.core.common.mock.di

import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.provider.MockAccessTokenProvider
import io.github.mudrichenkoevgeny.kmp.core.common.mock.platform.model.mockDeviceInfo
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.MockEncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Creates a [CommonComponent] wired with in-memory/mock implementations.
 *
 * This helper is intended for previews and non-platform unit tests.
 * It uses:
 * - [MockEncryptedSettings] for encrypted storage
 * - [MockAccessTokenProvider] for token access
 * - [mockDeviceInfo] for deterministic headers/device fields
 *
 * @param clientType Value passed to [mockDeviceInfo] for deterministic device metadata.
 * @param platformContext Optional handle for platform services (for example Android [Context] for [CommonComponent.externalLauncher]).
 * @return A [CommonComponent] that does not require platform encrypted storage or a real base URL.
 */
@OptIn(InternalApi::class)
fun mockCommonComponent(
    clientType: UserClientType = UserClientType.ANDROID,
    platformContext: Any? = null
): CommonComponent {
    return CommonComponent(
        encryptedSettings = MockEncryptedSettings(),
        deviceInfo = mockDeviceInfo(clientType = clientType),
        baseUrl = "",
        httpClientConfigPlugins = emptyList(),
        accessTokenProvider = MockAccessTokenProvider(),
        appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
        platformContext = platformContext
    )
}