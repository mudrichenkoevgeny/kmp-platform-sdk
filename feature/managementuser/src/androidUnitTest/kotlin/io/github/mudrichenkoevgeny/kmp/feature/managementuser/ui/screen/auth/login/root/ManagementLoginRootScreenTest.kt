package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalCommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.di.commonComponentMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.auth.login.root.ManagementLoginRootComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class ManagementLoginRootScreenTest {

    @Test
    fun rendersInitialWelcomeChild() = runComposeUiTest {
        val component = ManagementLoginRootComponentMock()

        setContent {
            CompositionLocalProvider(LocalCommonComponent provides commonComponentMock()) {
                ComponentTestHarness {
                    ManagementLoginRootScreen(component)
                }
            }
        }

        onNodeWithTag("LoginWelcome_Title").assertIsDisplayed()
    }
}
