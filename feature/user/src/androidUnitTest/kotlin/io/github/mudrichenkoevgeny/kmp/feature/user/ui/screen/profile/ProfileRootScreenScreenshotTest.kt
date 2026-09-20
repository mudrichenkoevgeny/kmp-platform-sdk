package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile

import android.app.Application
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.captureAppScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.ProfileRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.main.MainProfileComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileScreenState
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(
    sdk = [ROBOLECTRIC_SDK],
    application = Application::class,
    qualifiers = RobolectricDeviceQualifiers.Pixel5
)
class ProfileRootScreenScreenshotTest {

    @Test
    fun capture() = runComposeUiTest {
        val mainMock = MainProfileComponentMock(
            initialState = MainProfileScreenState.Content(user = userDetailsMock())
        )
        val component = ProfileRootComponentMock(
            initialChild = ProfileRootComponent.Child.Main(mainMock)
        )

        setContent {
            ComponentTestHarness {
                ProfileRootScreen(component)
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@ProfileRootScreenScreenshotTest,
            stateName = "Main"
        )
    }
}
