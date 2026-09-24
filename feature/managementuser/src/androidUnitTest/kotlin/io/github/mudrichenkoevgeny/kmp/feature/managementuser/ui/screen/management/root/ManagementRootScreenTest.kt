package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.root.ManagementRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main.MainManagementTestTags
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class ManagementRootScreenTest {

    @Test
    fun rendersInitialMainChild() = runComposeUiTest {
        val component = ManagementRootComponentMock()

        setContent {
            ComponentTestHarness {
                ManagementRootScreen(component)
            }
        }

        onNodeWithTag(MainManagementTestTags.TITLE).assertIsDisplayed()
    }
}