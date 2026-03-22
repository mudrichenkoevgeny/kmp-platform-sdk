package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class AuthProviderButtonTest {

    @Test
    fun email_displaysLabelAndClick() = runComposeUiTest {
        var clicks = 0
        setContent {
            MaterialTheme {
                Box(
                    modifier = Modifier
                        .padding(Dimens.paddingLarge)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AuthProviderButton(
                        authProvider = UserAuthProvider.EMAIL,
                        onClick = { clicks++ }
                    )
                }
            }
        }
        onNodeWithText(SIGN_IN_WITH_EMAIL).assertIsDisplayed()
        onNodeWithText(SIGN_IN_WITH_EMAIL).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, clicks)
    }

    @Test
    fun phone_displaysLabelAndClick() = runComposeUiTest {
        var clicks = 0
        setContent {
            MaterialTheme {
                Box(
                    modifier = Modifier
                        .padding(Dimens.paddingLarge)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AuthProviderButton(
                        authProvider = UserAuthProvider.PHONE,
                        onClick = { clicks++ }
                    )
                }
            }
        }
        onNodeWithText(SIGN_IN_WITH_PHONE).assertIsDisplayed()
        onNodeWithText(SIGN_IN_WITH_PHONE).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, clicks)
    }

    @Test
    fun google_displaysLabelAndClick() = runComposeUiTest {
        var clicks = 0
        setContent {
            MaterialTheme {
                Box(
                    modifier = Modifier
                        .padding(Dimens.paddingLarge)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AuthProviderButton(
                        authProvider = UserAuthProvider.GOOGLE,
                        onClick = { clicks++ }
                    )
                }
            }
        }
        onNodeWithText(SIGN_IN_WITH_GOOGLE).assertIsDisplayed()
        onNodeWithText(SIGN_IN_WITH_GOOGLE).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, clicks)
    }

    @Test
    fun apple_displaysLabelAndClick() = runComposeUiTest {
        var clicks = 0
        setContent {
            MaterialTheme {
                Box(
                    modifier = Modifier
                        .padding(Dimens.paddingLarge)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AuthProviderButton(
                        authProvider = UserAuthProvider.APPLE,
                        onClick = { clicks++ }
                    )
                }
            }
        }
        onNodeWithText(SIGN_IN_WITH_APPLE).assertIsDisplayed()
        onNodeWithText(SIGN_IN_WITH_APPLE).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, clicks)
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1

        /** Mirrors `values/strings.xml`. */
        const val SIGN_IN_WITH_EMAIL = "Sign in with Email"
        const val SIGN_IN_WITH_PHONE = "Sign in with Phone"
        const val SIGN_IN_WITH_GOOGLE = "Sign in with Google"
        const val SIGN_IN_WITH_APPLE = "Sign in with Apple"
    }
}
