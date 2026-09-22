package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.settings.ManagementSettingsRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.main.MainManagementSettingsTestTags
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.root.ManagementSettingsRootScreen
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class ManagementSettingsRootScreenTest {

    @Test
    fun rendersInitialMainChild() = runComposeUiTest {
        val component = ManagementSettingsRootComponentMock()

        setContent {
            ComponentTestHarness {
                ManagementSettingsRootScreen(component)
            }
        }

        onNodeWithTag(MainManagementSettingsTestTags.TITLE).assertIsDisplayed()
    }
}
