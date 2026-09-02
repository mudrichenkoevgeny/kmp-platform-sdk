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
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.main.MainProfileComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.totp.TotpSettingsComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.session.SessionListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.identifier.IdentifierListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileTestTags
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.SessionListTestTags
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.IdentifierListTestTags
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.TotpSettingsScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.TotpSettingsTestTags
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
    fun displaysTotpSettingsChild_whenActiveConfigurationIsTotpSettings() = runComposeUiTest {
        val totpComponent = TotpSettingsComponentMock(
            initialState = TotpSettingsScreenState.Disabled()
        )
        val rootComponent = ProfileRootComponentMock(
            initialChild = ProfileRootComponent.Child.TotpSettings(totpComponent),
            initialConfiguration = ProfileDestination.TotpSettings
        )

        setContent {
            ComponentTestHarness {
                ProfileRootScreen(rootComponent)
            }
        }

        onNodeWithTag(TotpSettingsTestTags.BACK_BUTTON).assertIsDisplayed()
    }

    @Test
    fun displaysSessionsChild_whenActiveConfigurationIsSessions() = runComposeUiTest {
        val sessionComponent = SessionListComponentMock()
        val rootComponent = ProfileRootComponentMock(
            initialChild = ProfileRootComponent.Child.Sessions(sessionComponent),
            initialConfiguration = ProfileDestination.Sessions
        )

        setContent {
            ComponentTestHarness {
                ProfileRootScreen(rootComponent)
            }
        }

        onNodeWithTag(SessionListTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(SessionListTestTags.BACK_BUTTON).assertIsDisplayed().performClick()

        assertEquals(EXPECTED_SINGLE_CALLBACK, sessionComponent.backCalls)
    }

    @Test
    fun displaysIdentifiersChild_whenActiveConfigurationIsIdentifiers() = runComposeUiTest {
        val identifierComponent = IdentifierListComponentMock()
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