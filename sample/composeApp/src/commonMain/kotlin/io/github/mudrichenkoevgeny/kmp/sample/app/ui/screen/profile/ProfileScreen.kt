package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.model.user.mockCurrentUser

/**
 * Profile tab: binds [ProfileScreenComponent] state to [ProfileContent].
 *
 * @param component Decompose component exposing [ProfileScreenState] and login intent.
 */
@Composable
fun ProfileScreen(component: ProfileScreenComponent) {
    val state by component.state.subscribeAsState()

    ProfileContent(
        state = state,
        onLoginClick = component::onLoginClick
    )
}

/**
 * Stateless profile UI for previews and tests.
 *
 * @param state Current authorization and content state.
 * @param onLoginClick Opens the login flow when in [ProfileScreenState.Unauthorized].
 */
@Composable
fun ProfileContent(
    state: ProfileScreenState,
    onLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is ProfileScreenState.Loading -> {
                CircularProgressIndicator()
            }
            is ProfileScreenState.Unauthorized -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Not authorized!")
                    Spacer(Modifier.height(Dimens.paddingMedium))
                    Button(onClick = onLoginClick) {
                        Text("Login")
                    }
                }
            }
            is ProfileScreenState.Content -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Authorized!")
                }
            }
            is ProfileScreenState.Error -> {
                Text(text = "Error")
            }
        }
    }
}

@Preview(showBackground = true, name = "Authorized")
@Composable
private fun ProfileContentContentPreview() {
    MaterialTheme {
        ProfileContent(
            state = ProfileScreenState.Content(
                user = mockCurrentUser()
            ),
            onLoginClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Unauthorized")
@Composable
private fun ProfileContentUnauthorizedPreview() {
    MaterialTheme {
        ProfileContent(
            state = ProfileScreenState.Unauthorized,
            onLoginClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun ProfileContentLoadingPreview() {
    MaterialTheme {
        ProfileContent(
            state = ProfileScreenState.Loading,
            onLoginClick = {}
        )
    }
}