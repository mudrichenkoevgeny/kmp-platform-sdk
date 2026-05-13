package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.sample.app.mock.di.appComponentMock
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.test.withSampleUiMainDispatcher
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertIs

@OptIn(InternalApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class MainScreenComponentImplTest {

    @Test
    fun initialStackActiveChildIsHome() = withSampleUiMainDispatcher {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val ctx = DefaultComponentContext(lifecycle)
        val app = appComponentMock(platformContext = context)
        val main = MainScreenComponentImpl(componentContext = ctx, appComponent = app)

        val active = main.stack.value.active
        assertIs<MainScreenComponent.Config.Home>(active.configuration)
        assertIs<MainScreenComponent.Child.HomeChild>(active.instance)

        lifecycle.destroy()
    }

    @Test
    fun onTabClickProfile_switchesActiveChildToProfile() = withSampleUiMainDispatcher {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val ctx = DefaultComponentContext(lifecycle)
        val app = appComponentMock(platformContext = context)
        val main = MainScreenComponentImpl(componentContext = ctx, appComponent = app)

        main.onTabClick(MainScreenComponent.Config.Profile)

        val active = main.stack.value.active
        assertIs<MainScreenComponent.Config.Profile>(active.configuration)
        assertIs<MainScreenComponent.Child.ProfileChild>(active.instance)

        lifecycle.destroy()
    }
}