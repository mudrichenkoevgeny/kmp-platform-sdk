package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.*
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.selection.UnlockMethodSelectionComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnlockMethodSelectionScreen(component: UnlockMethodSelectionComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.unlock_choose_method),
                        modifier = Modifier.testTag(UnlockMethodSelectionTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(UnlockMethodSelectionTestTags.BACK_BUTTON)
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(CoreTheme.dimens.paddingLarge)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.isEmailAvailable) {
                        OutlinedTextField(
                            value = state.emailInput,
                            onValueChange = component::onEmailInputChanged,
                            label = { Text(text = stringResource(Res.string.unlock_by_email)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(UnlockMethodSelectionTestTags.EMAIL_INPUT)
                        )

                        Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

                        Button(
                            onClick = component::onSelectEmailUnlock,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(UnlockMethodSelectionTestTags.UNLOCK_EMAIL_BUTTON),
                            enabled = !state.actionLoading && state.emailInput.isNotBlank()
                        ) {
                            Text(text = stringResource(Res.string.unlock_by_email))
                        }

                        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))
                    }

                    if (state.isPhoneAvailable) {
                        OutlinedTextField(
                            value = state.phoneInput,
                            onValueChange = component::onPhoneInputChanged,
                            label = { Text(text = stringResource(Res.string.unlock_by_phone)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(UnlockMethodSelectionTestTags.PHONE_INPUT)
                        )

                        Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

                        OutlinedButton(
                            onClick = component::onSelectPhoneUnlock,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(UnlockMethodSelectionTestTags.UNLOCK_PHONE_BUTTON),
                            enabled = !state.actionLoading && state.phoneInput.isNotBlank()
                        ) {
                            Text(text = stringResource(Res.string.unlock_by_phone))
                        }

                        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))
                    }

                    if (state.isGoogleAvailable) {
                        OutlinedButton(
                            onClick = component::onSelectGoogleUnlock,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(UnlockMethodSelectionTestTags.UNLOCK_GOOGLE_BUTTON),
                            enabled = !state.actionLoading
                        ) {
                            Text(text = stringResource(Res.string.unlock_by_google))
                        }

                        Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                    }

                    if (state.isAppleAvailable) {
                        OutlinedButton(
                            onClick = component::onSelectAppleUnlock,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(UnlockMethodSelectionTestTags.UNLOCK_APPLE_BUTTON),
                            enabled = !state.actionLoading
                        ) {
                            Text(text = stringResource(Res.string.unlock_by_apple))
                        }
                    }

                    ErrorText(state.actionError)
                }
            }

            if (state.actionLoading) {
                FullscreenLoading(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun ErrorText(error: AppError?) {
    AnimatedVisibility(
        visible = error != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        error?.let {
            Text(
                text = it.toLocalizedMessage(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = CoreTheme.dimens.paddingSmall)
                    .testTag(UnlockMethodSelectionTestTags.ACTION_ERROR_TEXT)
            )
        }
    }
}

internal object UnlockMethodSelectionTestTags {
    const val TITLE = "UnlockMethodSelection_Title"
    const val BACK_BUTTON = "UnlockMethodSelection_BackButton"
    const val EMAIL_INPUT = "UnlockMethodSelection_EmailInput"
    const val UNLOCK_EMAIL_BUTTON = "UnlockMethodSelection_UnlockEmailButton"
    const val PHONE_INPUT = "UnlockMethodSelection_PhoneInput"
    const val UNLOCK_PHONE_BUTTON = "UnlockMethodSelection_UnlockPhoneButton"
    const val UNLOCK_GOOGLE_BUTTON = "UnlockMethodSelection_UnlockGoogleButton"
    const val UNLOCK_APPLE_BUTTON = "UnlockMethodSelection_UnlockAppleButton"
    const val ACTION_ERROR_TEXT = "UnlockMethodSelection_ActionErrorText"
}

@InternalApi
internal class UnlockMethodSelectionPreviewProvider :
    PreviewParameterProvider<UnlockMethodSelectionScreenState> {

    private val items: List<Pair<String, UnlockMethodSelectionScreenState>> = listOf(
        "Default / Empty" to UnlockMethodSelectionScreenState(),
        "Prefilled Email & Phone" to UnlockMethodSelectionScreenState(
            emailInput = "user@example.com",
            phoneInput = "+79991234567"
        ),
        "Action Loading" to UnlockMethodSelectionScreenState(
            emailInput = "user@example.com",
            actionLoading = true
        ),
        "Action Error" to UnlockMethodSelectionScreenState(
            emailInput = "user@example.com",
            actionError = CommonError.Unknown()
        ),
        "Email Only Identifier" to UnlockMethodSelectionScreenState(
            emailInput = "user@example.com",
            knownIdentifiers = listOf(
                userIdentifierMock().copy(userAuthProvider = UserAuthProvider.EMAIL, identifier = "user@example.com")
            )
        )
    )

    override val values: Sequence<UnlockMethodSelectionScreenState> =
        items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? =
        items.getOrNull(index)?.first
}

@OptIn(InternalApi::class)
@Composable
private fun UnlockMethodSelectionScreenPreviewContent(state: UnlockMethodSelectionScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        Surface {
            UnlockMethodSelectionScreen(
                component = UnlockMethodSelectionComponentMock(initialState = state)
            )
        }
    }
}

private val defaultPreviewState = UnlockMethodSelectionScreenState(
    emailInput = "user@example.com",
    phoneInput = "+79991234567"
)

@OptIn(InternalApi::class)
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(UnlockMethodSelectionPreviewProvider::class) state: UnlockMethodSelectionScreenState
) {
    ScreenPreviewContainer {
        UnlockMethodSelectionScreenPreviewContent(state = state)
    }
}

@OptIn(InternalApi::class)
@ScreenSizePreviews
@Composable
private fun AdaptivePreview() {
    ScreenPreviewContainer {
        UnlockMethodSelectionScreenPreviewContent(state = defaultPreviewState)
    }
}

@OptIn(InternalApi::class)
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        UnlockMethodSelectionScreenPreviewContent(state = defaultPreviewState)
    }
}

@OptIn(InternalApi::class)
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        UnlockMethodSelectionScreenPreviewContent(state = defaultPreviewState)
    }
}
