package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class AuthProviderGridTest {

    @Test
    fun singleProvider_click_invokesCallbackWithMatchingProvider() = runComposeUiTest {
        val clicked = mutableListOf<UserAuthProvider>()
        val providers = listOf(UserAuthProvider.GOOGLE)
        setContent {
            MaterialTheme {
                Box(
                    modifier = Modifier
                        .padding(Dimens.paddingLarge)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AuthProviderGrid(
                        authProviders = providers,
                        onProviderClick = { clicked.add(it) }
                    )
                }
            }
        }
        onNode(hasClickAction(), useUnmergedTree = true).performClick()
        assertContentEquals(providers, clicked)
    }

    @Test
    fun emptyList_rendersNoClickTargets() = runComposeUiTest {
        var clicks = 0
        setContent {
            MaterialTheme {
                Box(
                    modifier = Modifier
                        .padding(Dimens.paddingLarge)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AuthProviderGrid(
                        authProviders = emptyList(),
                        onProviderClick = { clicks++ }
                    )
                }
            }
        }
        assertFailsWith<AssertionError> {
            onNode(hasClickAction(), useUnmergedTree = true).performClick()
        }
        assertEquals(0, clicks)
    }
}
