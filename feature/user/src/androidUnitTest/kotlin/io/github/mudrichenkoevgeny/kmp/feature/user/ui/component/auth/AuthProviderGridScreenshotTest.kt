package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.captureAppScreen
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
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
class AuthProviderGridScreenshotTest(
    private val stateName: String,
    private val providers: List<UserAuthProvider>
) {

    @Test
    fun capture() = runComposeUiTest {
        setContent {
            ComponentTestHarness {
                Surface {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(CoreTheme.dimens.paddingLarge),
                        contentAlignment = Alignment.Center
                    ) {
                        AuthProviderGrid(
                            authProviders = providers,
                            onProviderClick = {}
                        )
                    }
                }
            }
        }

        onRoot().captureAppScreen(
            testInstance = this@AuthProviderGridScreenshotTest,
            stateName = stateName
        )
    }

    companion object {
        @Suppress("Unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf("TwoProviders", listOf(UserAuthProvider.GOOGLE, UserAuthProvider.APPLE)),
                arrayOf(
                    "AllProviders",
                    listOf(
                        UserAuthProvider.EMAIL,
                        UserAuthProvider.PHONE,
                        UserAuthProvider.GOOGLE,
                        UserAuthProvider.APPLE
                    )
                )
            )
        }
    }
}
