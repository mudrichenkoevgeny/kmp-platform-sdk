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