package io.github.mudrichenkoevgeny.kmp.feature.user.mock.di

import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.di.mockCommonComponent
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.security.mock.di.mockSecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.di.mockSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.auth.UserAuthServicesMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.storage.auth.MockAuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage

/**
 * Builds a [UserComponent] backed by in-memory or fake collaborators for previews and tests.
 *
 * Callers outside the SDK should opt in to [InternalApi] when using core mock factories this wires through.
 *
 * @param commonComponent Defaults to `mockCommonComponent()`.
 * @param settingsComponent Defaults to `mockSettingsComponent()`.
 * @param securityComponent Defaults to `mockSecurityComponent()`.
 * @param authStorage Defaults to [MockAuthStorage].
 * @param authServices Defaults to [UserAuthServicesMock].
 * @return A fully wired user feature root using the provided or default mocks.
 */
@OptIn(InternalApi::class)
fun mockUserComponent(
    commonComponent: CommonComponent = mockCommonComponent(),
    settingsComponent: SettingsComponent = mockSettingsComponent(),
    securityComponent: SecurityComponent = mockSecurityComponent(),
    authStorage: AuthStorage = MockAuthStorage(),
    authServices: UserAuthServices = UserAuthServicesMock()
): UserComponent {
    return UserComponent(
        commonComponent = commonComponent,
        settingsComponent = settingsComponent,
        securityComponent = securityComponent,
        authStorage = authStorage,
        authServices = authServices
    )
}