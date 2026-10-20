package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.detail

import android.app.Application
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.testing.ui.test.captureAppScreen
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierPrivate
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.identifier.detail.IdentifierDetailComponentMock
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
class IdentifierDetailScreenScreenshotTest(
    private val stateName: String,
    private val state: IdentifierDetailScreenState
) {

    @Test
    fun capture() = runComposeUiTest {
        val component = IdentifierDetailComponentMock(initialState = state)
        setContent {
            ComponentTestHarness {
                IdentifierDetailScreen(component)
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@IdentifierDetailScreenScreenshotTest,
            stateName = stateName
        )
    }

    companion object {
        @Suppress("Unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            val mockIdentifier = createTestUserIdentifierPrivate()
            return listOf(
                arrayOf("Loading", IdentifierDetailScreenState.Loading),
                arrayOf("Error", IdentifierDetailScreenState.Error(CommonError.Unknown())),
                arrayOf(
                    "Content_CurrentIdentifier",
                    IdentifierDetailScreenState.Content(
                        identifier = mockIdentifier,
                        isCurrentIdentifier = true,
                        canChangePassword = true,
                        canDeletePassword = false
                    )
                ),
                arrayOf(
                    "Content_OtherIdentifier",
                    IdentifierDetailScreenState.Content(
                        identifier = mockIdentifier,
                        isCurrentIdentifier = false,
                        canChangePassword = false,
                        canDeletePassword = true
                    )
                )
            )
        }
    }
}
