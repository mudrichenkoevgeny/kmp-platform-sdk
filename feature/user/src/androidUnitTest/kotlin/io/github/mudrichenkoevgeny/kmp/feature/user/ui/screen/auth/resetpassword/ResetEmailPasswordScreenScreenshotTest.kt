package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword

import android.app.Application
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoadingConfig
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.captureAppScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.resetpassword.ResetEmailPasswordComponentMock
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
class ResetEmailPasswordScreenScreenshotTest(
    private val stateName: String,
    private val state: ResetEmailPasswordScreenState
) {

    @Test
    fun capture() = runComposeUiTest {
        val component = ResetEmailPasswordComponentMock(initialState = state)

        setContent {
            ComponentTestHarness {
                ResetEmailPasswordScreen(component)
            }
        }

        if ((state is ResetEmailPasswordScreenState.Loading) ||
            (state is ResetEmailPasswordScreenState.EmailInput && state.actionLoading) ||
            (state is ResetEmailPasswordScreenState.ResetInput && state.actionLoading)
        ) {
            mainClock.autoAdvance = false
            mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + 50L)
        }

        onRoot().captureAppScreen(
            testInstance = this@ResetEmailPasswordScreenScreenshotTest,
            stateName = stateName
        )
    }

    companion object {
        @Suppress("Unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            val provider = ResetEmailPasswordPreviewProvider()
            return provider.values.mapIndexed { index, state ->
                val name = provider.getDisplayName(index) ?: "State_$index"
                arrayOf(name, state)
            }.toList()
        }
    }
}
