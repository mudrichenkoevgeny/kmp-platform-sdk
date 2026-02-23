package io.github.mudrichenkoevgeny.kmp.sample.app.mock.di

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.di.mockCommonComponent
import io.github.mudrichenkoevgeny.kmp.core.security.mock.di.mockSecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.di.mockSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.di.mockUserComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.di.AppComponent

@OptIn(InternalApi::class)
fun mockAppComponent(): AppComponent {
    return AppComponent(
        mockCommonComponent = mockCommonComponent(),
        mockSettingsComponent = mockSettingsComponent(),
        mockSecurityComponent = mockSecurityComponent(),
        mockUserComponent = mockUserComponent()
    )
}