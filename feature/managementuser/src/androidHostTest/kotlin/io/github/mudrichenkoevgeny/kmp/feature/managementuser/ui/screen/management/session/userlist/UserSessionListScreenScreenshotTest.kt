package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist

import android.app.Application
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoadingConfig
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.testing.ui.test.captureAppScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.session.SessionListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListPreviewProvider
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListScreenState
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.test.Test

@InternalApi
@RunWith(ParameterizedRobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
    sdk = [ROBOLECTRIC_SDK],
    application = Application::class,
    qualifiers = RobolectricDeviceQualifiers.Pixel5
)
class UserSessionListScreenScreenshotTest(
    private val stateName: String,
    private val state: SessionListScreenState
) {

    @Test
    fun capture() = runComposeUiTest {
        val component = SessionListComponentMock(initialState = state)

        setContent {
            ComponentTestHarness {
                SessionListScreen(component)
            }
        }

        if ((state is SessionListScreenState.Loading) ||
            (state is SessionListScreenState.Content && state.actionLoading)
        ) {
            mainClock.autoAdvance = false
            mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + 50L)
        }

        onRoot().captureAppScreen(
            testInstance = this@UserSessionListScreenScreenshotTest,
            stateName = stateName
        )
    }

    companion object {
        @Suppress("Unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            val provider = SessionListPreviewProvider()
            return provider.values.mapIndexed { index, state ->
                val name = provider.getDisplayName(index) ?: "State_$index"
                arrayOf(name, state)
            }.toList()
        }
    }
}
