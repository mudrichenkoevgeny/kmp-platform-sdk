package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.sample.app.mock.di.mockAppComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.test.withSampleUiMainDispatcher
import kotlin.test.Test
import kotlin.test.assertIs

@OptIn(InternalApi::class)
class MainScreenComponentImplTest {

    @Test
    fun initialStackActiveChildIsHome() = withSampleUiMainDispatcher {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val ctx = DefaultComponentContext(lifecycle)
        val app = mockAppComponent()
        val main = MainScreenComponentImpl(componentContext = ctx, appComponent = app)
        val active = main.stack.value.active
        assertIs<MainScreenComponent.Config.Home>(active.configuration)
        assertIs<MainScreenComponent.Child.HomeChild>(active.instance)
        lifecycle.destroy()
    }

    @Test
    fun onTabClickProfile_switchesActiveChildToProfile() = withSampleUiMainDispatcher {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val ctx = DefaultComponentContext(lifecycle)
        val app = mockAppComponent()
        val main = MainScreenComponentImpl(componentContext = ctx, appComponent = app)
        main.onTabClick(MainScreenComponent.Config.Profile)
        val active = main.stack.value.active
        assertIs<MainScreenComponent.Config.Profile>(active.configuration)
        assertIs<MainScreenComponent.Child.ProfileChild>(active.instance)
        lifecycle.destroy()
    }
}
