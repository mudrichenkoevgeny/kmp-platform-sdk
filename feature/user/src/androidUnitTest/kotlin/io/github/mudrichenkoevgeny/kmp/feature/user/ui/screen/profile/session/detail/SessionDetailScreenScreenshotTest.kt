package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail

import android.app.Application
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoadingConfig
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.captureAppScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.session.detail.SessionDetailComponentMock
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
class SessionDetailScreenScreenshotTest(
    private val stateName: String,
    private val state: SessionDetailScreenState
) {

    @Test
    fun capture() = runComposeUiTest {
        val component = SessionDetailComponentMock(initialState = state)

        setContent {
            ComponentTestHarness {
                SessionDetailScreen(component)
            }
        }

        if ((state is SessionDetailScreenState.Loading) ||
            (state is SessionDetailScreenState.Content && state.actionLoading)
        ) {
            mainClock.autoAdvance = false
            mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + 50L)
        }

        onRoot().captureAppScreen(
            testInstance = this@SessionDetailScreenScreenshotTest,
            stateName = stateName
        )
    }

    companion object {
        @Suppress("Unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            val provider = SessionDetailPreviewProvider()
            return provider.values.mapIndexed { index, state ->
                val name = provider.getDisplayName(index) ?: "State_$index"
                arrayOf(name, state)
            }.toList()
        }
    }
}
