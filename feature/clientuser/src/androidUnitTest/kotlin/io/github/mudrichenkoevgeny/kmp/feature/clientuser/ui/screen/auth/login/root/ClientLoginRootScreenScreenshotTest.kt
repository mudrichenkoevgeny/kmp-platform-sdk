package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root

import android.app.Application
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalCommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.di.commonComponentMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.captureAppScreen
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.ui.screen.auth.login.root.ClientLoginRootComponentMock
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
class ClientLoginRootScreenScreenshotTest {

    @Test
    fun capture() = runComposeUiTest {
        val component = ClientLoginRootComponentMock()

        setContent {
            CompositionLocalProvider(LocalCommonComponent provides commonComponentMock()) {
                ComponentTestHarness {
                    ClientLoginRootScreen(component)
                }
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@ClientLoginRootScreenScreenshotTest,
            stateName = "Welcome"
        )
    }
}
