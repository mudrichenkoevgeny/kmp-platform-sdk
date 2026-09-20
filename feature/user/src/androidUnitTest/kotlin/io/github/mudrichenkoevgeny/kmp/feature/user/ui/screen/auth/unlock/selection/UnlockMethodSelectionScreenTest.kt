package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.selection.UnlockMethodSelectionComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class UnlockMethodSelectionScreenTest {

    @Test
    fun emailUnlock_bindsInputAndTriggersCallback() = runComposeUiTest {
        var emailUnlockClicked = false
        val initialState = UnlockMethodSelectionScreenState(
            knownIdentifiers = listOf(
                userIdentifierMock().copy(userAuthProvider = UserAuthProvider.EMAIL, identifier = "user@example.com")
            )
        )
        val component = object : UnlockMethodSelectionComponentMock(initialState = initialState) {
            override fun onSelectEmailUnlock() {
                emailUnlockClicked = true
            }
        }

        setContent {
            ComponentTestHarness {
                UnlockMethodSelectionScreen(component)
            }
        }

        onNodeWithTag(UnlockMethodSelectionTestTags.EMAIL_INPUT).assertIsDisplayed()
        onNodeWithTag(UnlockMethodSelectionTestTags.UNLOCK_EMAIL_BUTTON).assertIsNotEnabled()

        onNodeWithTag(UnlockMethodSelectionTestTags.EMAIL_INPUT).performTextReplacement("user@example.com")
        assertEquals("user@example.com", component.state.value.emailInput)
        onNodeWithTag(UnlockMethodSelectionTestTags.UNLOCK_EMAIL_BUTTON).assertIsEnabled()

        onNodeWithTag(UnlockMethodSelectionTestTags.UNLOCK_EMAIL_BUTTON).performClick()
        assertTrue(emailUnlockClicked)
    }

    @Test
    fun phoneUnlock_bindsInputAndTriggersCallback() = runComposeUiTest {
        var phoneUnlockClicked = false
        val initialState = UnlockMethodSelectionScreenState(
            knownIdentifiers = listOf(
                userIdentifierMock().copy(userAuthProvider = UserAuthProvider.PHONE, identifier = "+1234567890")
            )
        )
        val component = object : UnlockMethodSelectionComponentMock(initialState = initialState) {
            override fun onSelectPhoneUnlock() {
                phoneUnlockClicked = true
            }
        }

        setContent {
            ComponentTestHarness {
                UnlockMethodSelectionScreen(component)
            }
        }

        onNodeWithTag(UnlockMethodSelectionTestTags.PHONE_INPUT).assertIsDisplayed()
        onNodeWithTag(UnlockMethodSelectionTestTags.UNLOCK_PHONE_BUTTON).assertIsNotEnabled()

        onNodeWithTag(UnlockMethodSelectionTestTags.PHONE_INPUT).performTextReplacement("+1234567890")
        assertEquals("+1234567890", component.state.value.phoneInput)
        onNodeWithTag(UnlockMethodSelectionTestTags.UNLOCK_PHONE_BUTTON).assertIsEnabled()

        onNodeWithTag(UnlockMethodSelectionTestTags.UNLOCK_PHONE_BUTTON).performClick()
        assertTrue(phoneUnlockClicked)
    }

    @Test
    fun googleUnlock_triggersCallback() = runComposeUiTest {
        var googleUnlockClicked = false
        val initialState = UnlockMethodSelectionScreenState(
            knownIdentifiers = listOf(
                userIdentifierMock().copy(userAuthProvider = UserAuthProvider.GOOGLE)
            )
        )
        val component = object : UnlockMethodSelectionComponentMock(initialState = initialState) {
            override fun onSelectGoogleUnlock() {
                googleUnlockClicked = true
            }
        }

        setContent {
            ComponentTestHarness {
                UnlockMethodSelectionScreen(component)
            }
        }

        onNodeWithTag(UnlockMethodSelectionTestTags.UNLOCK_GOOGLE_BUTTON).assertIsDisplayed()
        onNodeWithTag(UnlockMethodSelectionTestTags.UNLOCK_GOOGLE_BUTTON).performClick()
        assertTrue(googleUnlockClicked)
    }
}
