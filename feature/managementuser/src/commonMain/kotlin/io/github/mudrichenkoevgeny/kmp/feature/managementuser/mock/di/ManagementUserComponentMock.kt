package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.di

import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.di.commonComponentMock
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.security.mock.di.securityComponentMock
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.di.settingsComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.di.ManagementUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth.UserAuthServicesMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.AuthStorageMock
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage

/**
 * Callers outside the SDK should opt in to [InternalApi] when using core mock factories this wires through.
 *
 * @param commonComponent Defaults to `mockCommonComponent()`.
 * @param settingsComponent Defaults to `mockSettingsComponent()`.
 * @param securityComponent Defaults to `mockSecurityComponent()`.
 * @param authStorage Defaults to [AuthStorageMock].
 * @param authServices Defaults to [UserAuthServicesMock].
 * @return A fully wired user feature root using the provided or default mocks.
 */

@InternalApi
fun managementUserComponentMock(
    commonComponent: CommonComponent = commonComponentMock(),
    settingsComponent: SettingsComponent = settingsComponentMock(),
    securityComponent: SecurityComponent = securityComponentMock(),
    authStorage: AuthStorage = AuthStorageMock(),
    authServices: UserAuthServices = UserAuthServicesMock()
): ManagementUserComponent {
    return ManagementUserComponent(
        commonComponent = commonComponent,
        settingsComponent = settingsComponent,
        securityComponent = securityComponent,
        authStorage = authStorage,
        authServices = authServices
    )
}