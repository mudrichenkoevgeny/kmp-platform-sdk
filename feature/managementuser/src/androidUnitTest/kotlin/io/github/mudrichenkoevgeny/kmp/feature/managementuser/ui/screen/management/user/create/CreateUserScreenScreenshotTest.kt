package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create

import android.app.Application
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.captureAppScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.create.CreateUserComponentMock
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
class CreateUserScreenScreenshotTest(
    private val stateName: String,
    private val state: CreateUserScreenState
) {

    @Test
    fun capture() = runComposeUiTest {
        val component = CreateUserComponentMock(initialState = state)

        setContent {
            ComponentTestHarness {
                CreateUserScreen(component)
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@CreateUserScreenScreenshotTest,
            stateName = stateName
        )
    }

    companion object {
        @Suppress("Unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            val provider = CreateUserPreviewProvider()
            return provider.values.mapIndexed { index, state ->
                val name = provider.getDisplayName(index) ?: "State_$index"
                arrayOf(name, state)
            }.toList()
        }
    }
}
