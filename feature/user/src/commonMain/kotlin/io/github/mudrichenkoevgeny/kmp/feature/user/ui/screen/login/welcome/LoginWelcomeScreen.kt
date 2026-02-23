package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.MockAppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error.FullscreenError
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenOverlayLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth.AuthProviderButton
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth.AuthProviderGrid
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.legal.LegalFooter
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginWelcomeScreen(component: LoginWelcomeComponent) {
    val state by component.state.subscribeAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val currentState = state) {
            is LoginWelcomeScreenState.Loading -> {
                FullscreenLoading()
            }
            is LoginWelcomeScreenState.InitializationError -> {
                FullscreenError(
                    error = currentState.error,
                    onRetry = component::onRetryInitClick
                )
            }
            is LoginWelcomeScreenState.Content -> {
                LoginWelcomeContent(
                    state = currentState,
                    onLoginClick = component::onLoginClick,
                    onPrivacyPolicyClick = component::onPrivacyPolicyClick,
                    onTermsOfServiceClick = component::onTermsOfServiceClick
                )
            }
        }
    }
}

@Composable
private fun LoginWelcomeContent(
    state: LoginWelcomeScreenState.Content,
    onLoginClick: (UserAuthProvider) -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.sign_in),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(Dimens.paddingLarge))

            state.availableAuthProviders.primary.forEach { provider ->
                AuthProviderButton(
                    authProvider = provider,
                    onClick = { onLoginClick(provider) },
                    modifier = Modifier.padding(bottom = Dimens.paddingSmall)
                )
            }

            if (state.availableAuthProviders.primary.isNotEmpty() &&
                state.availableAuthProviders.secondary.isNotEmpty()) {

                Text(
                    text = stringResource(Res.string.or_sign_in_with),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(vertical = Dimens.paddingMedium)
                )
            }

            AuthProviderGrid(
                authProviders = state.availableAuthProviders.secondary,
                onProviderClick = onLoginClick
            )

            AnimatedVisibility(
                visible = state.actionError != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                state.actionError?.let { error ->
                    Text(
                        text = error.toLocalizedMessage(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = Dimens.paddingMedium)
                    )
                }
            }

            Spacer(Modifier.height(Dimens.paddingLarge))

            LegalFooter(
                isPrivacyPolicyVisible = state.hasPrivacyPolicy,
                isTermsOfServiceVisible = state.hasTermsOfService,
                onPrivacyPolicyClick = onPrivacyPolicyClick,
                onTermsOfServiceClick = onTermsOfServiceClick,
            )
        }

        if (state.actionLoading) {
            FullscreenOverlayLoading()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginWelcomeScreenContentPreview() {
    val mockProviders = AvailableAuthProviders(
        primary = listOf(UserAuthProvider.EMAIL, UserAuthProvider.PHONE),
        secondary = listOf(UserAuthProvider.GOOGLE, UserAuthProvider.APPLE)
    )

    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            Surface {
                LoginWelcomeContent(
                    state = LoginWelcomeScreenState.Content(
                        availableAuthProviders = mockProviders,
                        actionError = null
                    ),
                    onLoginClick = {},
                    onPrivacyPolicyClick = {},
                    onTermsOfServiceClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginWelcomeScreenContentWithActionErrorPreview() {
    val mockProviders = AvailableAuthProviders(
        primary = listOf(UserAuthProvider.EMAIL, UserAuthProvider.PHONE),
        secondary = listOf(UserAuthProvider.GOOGLE, UserAuthProvider.APPLE)
    )

    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            Surface {
                LoginWelcomeContent(
                    state = LoginWelcomeScreenState.Content(
                        availableAuthProviders = mockProviders,
                        actionError = CommonError.Unknown()
                    ),
                    onLoginClick = {},
                    onPrivacyPolicyClick = {},
                    onTermsOfServiceClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginWelcomeScreenContentWithActionLoadingPreview() {
    val mockProviders = AvailableAuthProviders(
        primary = listOf(UserAuthProvider.EMAIL, UserAuthProvider.PHONE),
        secondary = listOf(UserAuthProvider.GOOGLE, UserAuthProvider.APPLE)
    )

    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            Surface {
                LoginWelcomeContent(
                    state = LoginWelcomeScreenState.Content(
                        availableAuthProviders = mockProviders,
                        actionLoading = true
                    ),
                    onLoginClick = {},
                    onPrivacyPolicyClick = {},
                    onTermsOfServiceClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginWelcomeScreenLoadingPreview() {
    MaterialTheme {
        Surface {
            Box(Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}