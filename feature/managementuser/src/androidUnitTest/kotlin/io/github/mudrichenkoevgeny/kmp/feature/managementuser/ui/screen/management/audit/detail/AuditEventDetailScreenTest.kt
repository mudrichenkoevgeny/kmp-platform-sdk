package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail

import android.app.Application
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoadingConfig
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.domain.model.event.auditEventMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.audit.detail.AuditEventDetailComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class AuditEventDetailScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = AuditEventDetailComponentMock(AuditEventDetailScreenState.Loading)
        setContent {
            ComponentTestHarness {
                AuditEventDetailScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun content_displaysElements() = runComposeUiTest {
        val event = auditEventMock()
        val component = AuditEventDetailComponentMock(AuditEventDetailScreenState.Content(event = event))
        setContent {
            ComponentTestHarness {
                AuditEventDetailScreen(component)
            }
        }
        onNodeWithTag(AuditEventDetailTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(AuditEventDetailTestTags.BACK_BUTTON).assertIsDisplayed()
    }

    @Test
    fun content_clickBack_invokesCallback() = runComposeUiTest {
        val event = auditEventMock()
        val component = AuditEventDetailComponentMock(AuditEventDetailScreenState.Content(event = event))
        setContent {
            ComponentTestHarness {
                AuditEventDetailScreen(component)
            }
        }
        onNodeWithTag(AuditEventDetailTestTags.BACK_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.backCalls)
    }

    @Test
    fun error_displaysErrorNode() = runComposeUiTest {
        val component = AuditEventDetailComponentMock(AuditEventDetailScreenState.Error(CommonError.Unknown()))
        setContent {
            ComponentTestHarness {
                AuditEventDetailScreen(component)
            }
        }
        onNodeWithTag(AuditEventDetailTestTags.GLOBAL_ERROR).assertIsDisplayed()
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}