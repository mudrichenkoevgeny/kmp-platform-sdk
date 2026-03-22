package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.di.mockCommonComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.di.mockUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.LoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeComponentImpl
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.test.withUserUiMainDispatcher
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
@OptIn(InternalApi::class)
class LoginRootComponentImplTest {

    @Test
    fun initialStack_activeConfiguration_isWelcome() {
        withUserUiMainDispatcher {
            val harness = createHarness()
            try {
                val active = harness.component.stack.value.active
                assertEquals(LoginDestination.Welcome, active.configuration)
            } finally {
                harness.destroy()
            }
        }
    }

    @Test
    fun initialStack_activeChild_isWelcomeWithLoginWelcomeComponentImpl() {
        withUserUiMainDispatcher {
            val harness = createHarness()
            try {
                val welcome =
                    assertIs<LoginRootComponent.Child.Welcome>(harness.component.stack.value.active.instance)
                assertIs<LoginWelcomeComponentImpl>(welcome.component)
            } finally {
                harness.destroy()
            }
        }
    }

    @Test
    fun onDismiss_invokesOnFinished() {
        withUserUiMainDispatcher {
            var finishedCalls = ZERO_CALLS
            val harness = createHarness(onFinished = { finishedCalls++ })
            try {
                harness.component.onDismiss()
                assertEquals(ONE_CALL, finishedCalls)
            } finally {
                harness.destroy()
            }
        }
    }

    @Test
    fun userComponent_createLoginRootDialogComponent_startsAtWelcome() {
        withUserUiMainDispatcher {
            val user = mockUserComponent(
                commonComponent = mockCommonComponent(platformContext = androidContext())
            )
            val lifecycle = LifecycleRegistry()
            lifecycle.resume()
            val ctx = DefaultComponentContext(lifecycle)
            try {
                val root = user.createLoginRootDialogComponent(ctx) {}
                assertEquals(LoginDestination.Welcome, root.stack.value.active.configuration)
                assertIs<LoginRootComponent.Child.Welcome>(root.stack.value.active.instance)
            } finally {
                lifecycle.destroy()
            }
        }
    }

    private fun androidContext(): Context = ApplicationProvider.getApplicationContext()

    private fun createHarness(onFinished: () -> Unit = {}): Harness {
        val user = mockUserComponent(
            commonComponent = mockCommonComponent(platformContext = androidContext())
        )
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()
        val ctx = DefaultComponentContext(lifecycle)
        val component = LoginRootComponentImpl(
            componentContext = ctx,
            settingsComponent = user.settingsComponent,
            securityComponent = user.securityComponent,
            userComponent = user,
            onFinished = onFinished
        )
        return Harness(lifecycle, component)
    }

    private class Harness(
        private val lifecycle: LifecycleRegistry,
        val component: LoginRootComponentImpl
    ) {
        fun destroy() {
            lifecycle.destroy()
        }
    }

    private companion object {
        const val ZERO_CALLS = 0
        const val ONE_CALL = 1
    }
}