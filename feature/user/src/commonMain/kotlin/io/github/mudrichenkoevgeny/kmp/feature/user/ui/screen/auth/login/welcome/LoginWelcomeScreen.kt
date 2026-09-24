package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error.FullscreenError
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenOverlayLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.DialogPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.DialogSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.welcome.LoginWelcomeComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.or_sign_in_with
import io.github.mudrichenkoevgeny.kmp.feature.user.sign_in
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth.AuthProviderButton
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.auth.AuthProviderGrid
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.legal.LegalFooter
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import org.jetbrains.compose.resources.stringResource

/**
 * Welcome UI: loading, fullscreen init error with retry, or provider list with legal footer and action feedback.
 *
 * @param component Decompose component driving state and callbacks.
 */
@Composable
fun LoginWelcomeScreen(component: LoginWelcomeComponent) {
    val state by component.state.subscribeAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
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
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(CoreTheme.dimens.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CoreScreenTitleText(
                text = stringResource(Res.string.sign_in),
                modifier = Modifier.testTag(LoginWelcomeTestTags.TITLE)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.availableAuthProviders.primary.forEach { provider ->
                    AuthProviderButton(
                        authProvider = provider,
                        onClick = { onLoginClick(provider) },
                        modifier = Modifier
                            .padding(bottom = CoreTheme.dimens.paddingSmall)
                            .testTag(LoginWelcomeTestTags.getAuthProviderTag(provider))
                    )
                }

                if (state.availableAuthProviders.primary.isNotEmpty() &&
                    state.availableAuthProviders.secondary.isNotEmpty()) {

                    CoreBodyText(
                        text = stringResource(Res.string.or_sign_in_with),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .padding(vertical = CoreTheme.dimens.paddingMedium)
                            .testTag(LoginWelcomeTestTags.OR_DIVIDER)
                    )
                }

                AuthProviderGrid(
                    authProviders = state.availableAuthProviders.secondary,
                    onProviderClick = onLoginClick,
                    modifier = Modifier.testTag(LoginWelcomeTestTags.SECONDARY_PROVIDERS_GRID)
                )

                AnimatedVisibility(
                    visible = state.actionError != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    state.actionError?.let { error ->
                        CoreErrorText(
                            text = error.toLocalizedMessage(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = CoreTheme.dimens.paddingMedium)
                                .testTag(LoginWelcomeTestTags.ACTION_ERROR_TEXT)
                        )
                    }
                }
            }

            LegalFooter(
                isPrivacyPolicyVisible = state.hasPrivacyPolicy,
                isTermsOfServiceVisible = state.hasTermsOfService,
                onPrivacyPolicyClick = onPrivacyPolicyClick,
                onTermsOfServiceClick = onTermsOfServiceClick
            )
        }

        if (state.actionLoading) {
            FullscreenOverlayLoading()
        }
    }
}

@InternalApi
internal class LoginWelcomePreviewProvider : PreviewParameterProvider<LoginWelcomeScreenState> {
    private val mockProviders = AvailableAuthProviders(
        primary = listOf(UserAuthProvider.EMAIL, UserAuthProvider.PHONE),
        secondary = listOf(UserAuthProvider.GOOGLE, UserAuthProvider.APPLE)
    )

    private val items: List<Pair<String, LoginWelcomeScreenState>> = listOf(
        "Default Content" to LoginWelcomeScreenState.Content(availableAuthProviders = mockProviders),
        "Content With Action Error" to LoginWelcomeScreenState.Content(
            availableAuthProviders = mockProviders,
            actionError = CommonError.Unknown()
        ),
        "Action Loading" to LoginWelcomeScreenState.Content(
            availableAuthProviders = mockProviders,
            actionLoading = true
        ),
        "Fullscreen Loading" to LoginWelcomeScreenState.Loading,
        "Initialization Error" to LoginWelcomeScreenState.InitializationError(CommonError.Unknown())
    )

    override val values: Sequence<LoginWelcomeScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun LoginWelcomeScreenPreviewContent(state: LoginWelcomeScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        Surface {
            LoginWelcomeScreen(
                component = LoginWelcomeComponentMock(initialState = state)
            )
        }
    }
}

private val defaultLoginWelcomePreviewState = LoginWelcomeScreenState.Content(
    availableAuthProviders = AvailableAuthProviders(
        primary = listOf(UserAuthProvider.EMAIL, UserAuthProvider.PHONE),
        secondary = listOf(UserAuthProvider.GOOGLE, UserAuthProvider.APPLE)
    )
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(LoginWelcomePreviewProvider::class) state: LoginWelcomeScreenState
) {
    DialogPreviewContainer {
        LoginWelcomeScreenPreviewContent(state = state)
    }
}

@InternalApi
@DialogSizePreviews
@Composable
private fun DialogSizePreview() {
    DialogPreviewContainer {
        LoginWelcomeScreenPreviewContent(state = defaultLoginWelcomePreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    DialogPreviewContainer {
        LoginWelcomeScreenPreviewContent(state = defaultLoginWelcomePreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    DialogPreviewContainer {
        LoginWelcomeScreenPreviewContent(state = defaultLoginWelcomePreviewState)
    }
}

internal object LoginWelcomeTestTags {
    const val TITLE = "LoginWelcome_Title"
    const val OR_DIVIDER = "LoginWelcome_OrDivider"
    const val SECONDARY_PROVIDERS_GRID = "LoginWelcome_SecondaryProvidersGrid"
    const val ACTION_ERROR_TEXT = "LoginWelcome_ActionErrorText"

    fun getAuthProviderTag(provider: UserAuthProvider): String = "LoginWelcome_AuthProvider_${provider.name}"
}
