package io.github.mudrichenkoevgeny.kmp.core.common.mock.di

import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.provider.AccessTokenProviderMock
import io.github.mudrichenkoevgeny.kmp.core.common.mock.platform.model.deviceInfoMock
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.EncryptedSettingsMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.client.ClientType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Creates a [CommonComponent] wired with in-memory/mock implementations.
 *
 * This helper is intended for previews and non-platform unit tests.
 * It uses:
 * - [EncryptedSettingsMock] for encrypted storage
 * - [AccessTokenProviderMock] for token access
 * - [deviceInfoMock] for deterministic headers/device fields
 *
 * @param clientType Value passed to [deviceInfoMock] for deterministic device metadata.
 * @param platformContext Optional handle for platform services (for example Android Context for [CommonComponent.externalLauncher]).
 * @return A [CommonComponent] that does not require platform encrypted storage or a real base URL.
 */
@InternalApi
fun commonComponentMock(
    clientType: ClientType = ClientType.ANDROID,
    platformContext: Any? = null
): CommonComponent {
    return CommonComponent(
        encryptedSettings = EncryptedSettingsMock(),
        deviceInfo = deviceInfoMock(clientType = clientType),
        baseUrl = "",
        httpClientConfigPlugins = emptyList(),
        accessTokenProvider = AccessTokenProviderMock(),
        appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
        platformContext = platformContext
    )
}