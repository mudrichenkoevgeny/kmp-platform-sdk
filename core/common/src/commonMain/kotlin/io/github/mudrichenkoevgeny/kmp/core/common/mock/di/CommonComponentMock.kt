package io.github.mudrichenkoevgeny.kmp.core.common.mock.di

import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.network.provider.MockAccessTokenProvider
import io.github.mudrichenkoevgeny.kmp.core.common.mock.platform.model.mockDeviceInfo
import io.github.mudrichenkoevgeny.kmp.core.common.mock.storage.MockEncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.UserClientType
import kotlinx.coroutines.test.TestScope

@OptIn(InternalApi::class)
fun mockCommonComponent(
    clientType: UserClientType = UserClientType.ANDROID
): CommonComponent {
    return CommonComponent(
        encryptedSettings = MockEncryptedSettings(),
        baseUrl = "",
        accessTokenProvider = MockAccessTokenProvider(),
        deviceInfo = mockDeviceInfo(clientType = clientType),
        appScope = TestScope()
    )
}