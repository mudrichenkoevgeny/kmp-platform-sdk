package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class AuthProviderItemTest {

    @Test
    fun click_invokesCallback() = runComposeUiTest {
        var clicks = 0
        setContent {
            MaterialTheme {
                Box(modifier = Modifier.padding(Dimens.paddingLarge)) {
                    AuthProviderItem(
                        authProvider = UserAuthProvider.EMAIL,
                        onClick = { clicks++ }
                    )
                }
            }
        }
        onNode(hasClickAction(), useUnmergedTree = true).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, clicks)
    }

    private companion object {
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
