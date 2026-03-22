package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root.LoginRootComponent
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.sample.app.mock.di.mockAppComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Login dialog slot activates [LoginRootComponent], which needs a platform [Context] for the
 * external launcher on Android JVM. Multiplatform [commonTest] has no application context; this test runs on Robolectric only.
 */
@OptIn(InternalApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class MainScreenComponentImplDialogSlotAndroidTest {

    @Test
    fun onShowLogin_thenDismiss_activatesAndClearsDialogSlot() {
        val dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
        try {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val lifecycle = LifecycleRegistry()
            lifecycle.resume()
            val ctx = DefaultComponentContext(lifecycle)
            val app = mockAppComponent(platformContext = context)
            val main = MainScreenComponentImpl(componentContext = ctx, appComponent = app)
            assertNull(main.loginDialogSlot.value.child?.instance)
            main.onShowLogin()
            assertNotNull(main.loginDialogSlot.value.child?.instance)
            main.onDismissLogin()
            assertNull(main.loginDialogSlot.value.child?.instance)
            lifecycle.destroy()
        } finally {
            Dispatchers.resetMain()
        }
    }
}
