package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.identifier.item

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.captureAppScreen
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
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
class IdentifierItemScreenshotTest(
    private val stateName: String,
    private val identifier: UserIdentifier,
    private val hasChangePassword: Boolean,
    private val enabled: Boolean
) {

    @Test
    fun capture() = runComposeUiTest {
        setContent {
            ComponentTestHarness {
                Surface {
                    Box(modifier = Modifier.padding(CoreTheme.dimens.paddingLarge)) {
                        IdentifierItem(
                            identifier = identifier,
                            onDeleteClick = {},
                            onChangePasswordClick = if (hasChangePassword) { {} } else null,
                            enabled = enabled
                        )
                    }
                }
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@IdentifierItemScreenshotTest,
            stateName = stateName
        )
    }

    companion object {
        @Suppress("Unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            val emailIdentifier = userIdentifierMock()
            val phoneIdentifier = userIdentifierMock().copy(
                userAuthProvider = UserAuthProvider.PHONE,
                identifier = "+1234567890"
            )
            return listOf(
                arrayOf("Email_With_Change_Password", emailIdentifier, true, true),
                arrayOf("Phone_Without_Change_Password", phoneIdentifier, false, true),
                arrayOf("Disabled", emailIdentifier, true, false)
            )
        }
    }
}
