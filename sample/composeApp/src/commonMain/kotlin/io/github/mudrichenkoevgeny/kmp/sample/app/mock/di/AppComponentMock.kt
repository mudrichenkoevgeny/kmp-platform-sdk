package io.github.mudrichenkoevgeny.kmp.sample.app.mock.di

import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.di.mockCommonComponent
import io.github.mudrichenkoevgeny.kmp.core.security.mock.di.mockSecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.di.mockSettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.di.mockUserComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.di.AppComponent

/**
 * Builds an [AppComponent] backed by SDK mock components (no real encrypted storage or network).
 *
 * @param platformContext Optional platform handle (for example Android application context) for components that need
 * an external launcher from [CommonComponent] when exercising login UI on JVM.
 * @return Initialized sample graph suitable for previews and lightweight tests.
 */
@OptIn(InternalApi::class)
fun mockAppComponent(platformContext: Any? = null): AppComponent {
    return AppComponent(
        mockCommonComponent = mockCommonComponent(platformContext = platformContext),
        mockSettingsComponent = mockSettingsComponent(),
        mockSecurityComponent = mockSecurityComponent(),
        mockUserComponent = mockUserComponent()
    )
}