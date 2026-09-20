package io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.root

import android.app.Application
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.captureAppScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.ui.screen.root.AuditApiRootComponentMock
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
class AuditApiRootScreenScreenshotTest {

    @Test
    fun capture() = runComposeUiTest {
        val component = AuditApiRootComponentMock()

        setContent {
            ComponentTestHarness {
                AuditApiRootScreen(component)
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@AuditApiRootScreenScreenshotTest,
            stateName = "Main"
        )
    }
}
