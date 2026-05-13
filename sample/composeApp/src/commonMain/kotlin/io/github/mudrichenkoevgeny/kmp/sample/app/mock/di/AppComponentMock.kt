package io.github.mudrichenkoevgeny.kmp.sample.app.mock.di

import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.di.commonComponentMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.di.securityComponentMock
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.di.settingsComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.di.userComponentMock
import io.github.mudrichenkoevgeny.kmp.sample.app.di.AppComponent

/**
 * Builds an [AppComponent] backed by SDK mock components (no real encrypted storage or network).
 *
 * @param platformContext Optional platform handle (for example Android application context) for components that need
 * an external launcher from [CommonComponent] when exercising login UI on JVM.
 * @return Initialized sample graph suitable for previews and lightweight tests.
 */
@InternalApi
fun appComponentMock(platformContext: Any? = null): AppComponent {
    val commonMock = commonComponentMock(platformContext = platformContext)
    val settingsMock = settingsComponentMock()
    val securityMock = securityComponentMock()

    return AppComponent(
        platformContext = platformContext,
        mockCommonComponent = commonMock,
        mockSettingsComponent = settingsMock,
        mockSecurityComponent = securityMock,
        mockUserComponent = userComponentMock(
            commonComponent = commonMock,
            settingsComponent = settingsMock,
            securityComponent = securityMock
        )
    )
}