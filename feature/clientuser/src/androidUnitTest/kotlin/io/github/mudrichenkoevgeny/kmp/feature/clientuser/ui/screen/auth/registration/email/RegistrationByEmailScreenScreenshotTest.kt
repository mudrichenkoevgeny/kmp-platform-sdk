package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email

import android.app.Application
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoadingConfig
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.captureAppScreen
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.ui.screen.auth.registration.email.RegistrationByEmailComponentMock
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
class RegistrationByEmailScreenScreenshotTest(
    private val stateName: String,
    private val state: RegistrationByEmailScreenState
) {

    @Test
    fun capture() = runComposeUiTest {
        val component = RegistrationByEmailComponentMock(initialState = state)

        setContent {
            ComponentTestHarness {
                RegistrationByEmailScreen(component)
            }
        }

        if (state.actionLoading) {
            mainClock.autoAdvance = false
            mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + 50L)
        }

        onRoot().captureAppScreen(
            testInstance = this@RegistrationByEmailScreenScreenshotTest,
            stateName = stateName
        )
    }

    companion object {
        @Suppress("Unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            val provider = RegistrationByEmailPreviewProvider()
            return provider.values.mapIndexed { index, state ->
                val name = provider.getDisplayName(index) ?: "State_$index"
                arrayOf(name, state)
            }.toList()
        }
    }
}