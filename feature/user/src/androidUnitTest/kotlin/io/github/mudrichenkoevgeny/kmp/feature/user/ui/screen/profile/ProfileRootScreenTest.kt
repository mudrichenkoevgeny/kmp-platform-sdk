package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.ProfileRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.identifier.SelfIdentifierListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.main.MainProfileComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.session.SelfSessionListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.session.detail.SessionDetailComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.totp.TotpMainComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.IdentifierListTestTags
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileTestTags
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.root.ProfileRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.root.ProfileRootScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailTestTags
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list.SelfSessionListTestTags
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainTestTags
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class ProfileRootScreenTest {

    @Test
    fun displaysMainChild_whenActiveConfigurationIsMain() = runComposeUiTest {
        val mainComponent = MainProfileComponentMock(
            initialState = MainProfileScreenState.Content(user = userDetailsMock())
        )
        val rootComponent = ProfileRootComponentMock(
            initialChild = ProfileRootComponent.Child.Main(mainComponent),
            initialConfiguration = ProfileDestination.Main
        )

        setContent {
            ComponentTestHarness {
                ProfileRootScreen(rootComponent)
            }
        }

        onNodeWithTag(MainProfileTestTags.USER_ID_TEXT).assertIsDisplayed()
    }

    @Test
    fun displaysTotpMainChild_whenActiveConfigurationIsTotpMain() = runComposeUiTest {
        val totpComponent = TotpMainComponentMock(
            initialState = TotpMainScreenState.Disabled()
        )
        val rootComponent = ProfileRootComponentMock(
            initialChild = ProfileRootComponent.Child.TotpMain(totpComponent),
            initialConfiguration = ProfileDestination.TotpMain
        )

        setContent {
            ComponentTestHarness {
                ProfileRootScreen(rootComponent)
            }
        }

        onNodeWithTag(TotpMainTestTags.BACK_BUTTON).assertIsDisplayed()
    }

    @Test
    fun displaysSessionsChild_whenActiveConfigurationIsSessions() = runComposeUiTest {
        val sessionComponent = SelfSessionListComponentMock()
        val rootComponent = ProfileRootComponentMock(
            initialChild = ProfileRootComponent.Child.Sessions(sessionComponent),
            initialConfiguration = ProfileDestination.Sessions
        )

        setContent {
            ComponentTestHarness {
                ProfileRootScreen(rootComponent)
            }
        }

        onNodeWithTag(SelfSessionListTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(SelfSessionListTestTags.BACK_BUTTON).assertIsDisplayed().performClick()

        assertEquals(EXPECTED_SINGLE_CALLBACK, sessionComponent.backCalls)
    }

    @Test
    fun displaysSessionDetailChild_whenActiveConfigurationIsSessionDetail() = runComposeUiTest {
        val sessionDetailComponent = SessionDetailComponentMock()
        val rootComponent = ProfileRootComponentMock(
            initialChild = ProfileRootComponent.Child.SessionDetail(sessionDetailComponent),
            initialConfiguration = ProfileDestination.SessionDetail("00000000-0000-0000-0000-000000000000")
        )

        setContent {
            ComponentTestHarness {
                ProfileRootScreen(rootComponent)
            }
        }

        onNodeWithTag(SessionDetailTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(SessionDetailTestTags.BACK_BUTTON).assertIsDisplayed().performClick()

        assertEquals(EXPECTED_SINGLE_CALLBACK, sessionDetailComponent.backCalls)
    }

    @Test
    fun displaysIdentifiersChild_whenActiveConfigurationIsIdentifiers() = runComposeUiTest {
        val identifierComponent = SelfIdentifierListComponentMock()
        val rootComponent = ProfileRootComponentMock(
            initialChild = ProfileRootComponent.Child.Identifiers(identifierComponent),
            initialConfiguration = ProfileDestination.Identifiers
        )

        setContent {
            ComponentTestHarness {
                ProfileRootScreen(rootComponent)
            }
        }

        onNodeWithTag(IdentifierListTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(IdentifierListTestTags.BACK_BUTTON).assertIsDisplayed().performClick()

        assertEquals(EXPECTED_SINGLE_CALLBACK, identifierComponent.backCalls)
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}