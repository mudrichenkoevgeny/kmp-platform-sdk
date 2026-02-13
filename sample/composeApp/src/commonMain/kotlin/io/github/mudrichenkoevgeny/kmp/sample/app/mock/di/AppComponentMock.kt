package io.github.mudrichenkoevgeny.kmp.sample.app.mock.di

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.di.mockCommonComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.di.AppComponent

@OptIn(InternalApi::class)
fun mockAppComponent(): AppComponent {
    return AppComponent(
        mockCommonComponent = mockCommonComponent()
    )
}