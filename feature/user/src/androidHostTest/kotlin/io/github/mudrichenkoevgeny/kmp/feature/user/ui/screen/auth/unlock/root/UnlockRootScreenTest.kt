package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.root

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.root.UnlockRootComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class UnlockRootScreenTest {

    @Test
    fun rendersInitialMethodSelectionChild() = runComposeUiTest {
        val component = UnlockRootComponentMock()

        setContent {
            ComponentTestHarness {
                UnlockRootScreen(component)
            }
        }

        onNodeWithTag("UnlockMethodSelection_Title").assertIsDisplayed()
    }
}
