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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
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
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.time.formatEpochMillisToDateTime
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.error_user_locked
import io.github.mudrichenkoevgeny.kmp.feature.user.error_user_locked_until
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.selection.UnlockMethodSelectionComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.unlock_by_apple
import io.github.mudrichenkoevgeny.kmp.feature.user.unlock_by_email
import io.github.mudrichenkoevgeny.kmp.feature.user.unlock_by_google
import io.github.mudrichenkoevgeny.kmp.feature.user.unlock_by_phone
import io.github.mudrichenkoevgeny.kmp.feature.user.unlock_choose_method
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
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
                    CoreScreenTitleText(
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
                    val lockoutText = when {
                        state.lockoutType == AccountLockoutType.TEMPORARY && state.lockoutUntil != null -> {
                            val formattedDateTime = formatEpochMillisToDateTime(state.lockoutUntil)
                            if (formattedDateTime != null) {
                                stringResource(Res.string.error_user_locked_until, formattedDateTime)
                            } else {
                                stringResource(Res.string.error_user_locked)
                            }
                        }
                        state.lockoutType != null && state.lockoutType != AccountLockoutType.NONE -> {
                            stringResource(Res.string.error_user_locked)
                        }
                        else -> null
                    }

                    if (lockoutText != null) {
                        CoreErrorText(
                            text = lockoutText,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(bottom = CoreTheme.dimens.paddingLarge)
                                .testTag(UnlockMethodSelectionTestTags.LOCKOUT_INFO_TEXT)
                        )
                    }

                    if (state.isEmailAvailable) {
                        CoreButton(
                            text = stringResource(Res.string.unlock_by_email),
                            onClick = component::onSelectEmailUnlock,
                            modifier = Modifier.testTag(UnlockMethodSelectionTestTags.UNLOCK_EMAIL_BUTTON),
                            enabled = !state.actionLoading
                        )

                        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))
                    }

                    if (state.isPhoneAvailable) {
                        CoreButton(
                            text = stringResource(Res.string.unlock_by_phone),
                            onClick = component::onSelectPhoneUnlock,
                            modifier = Modifier.testTag(UnlockMethodSelectionTestTags.UNLOCK_PHONE_BUTTON),
                            enabled = !state.actionLoading
                        )

                        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))
                    }

                    if (state.isGoogleAvailable) {
                        CoreButton(
                            text = stringResource(Res.string.unlock_by_google),
                            onClick = component::onSelectGoogleUnlock,
                            modifier = Modifier.testTag(UnlockMethodSelectionTestTags.UNLOCK_GOOGLE_BUTTON),
                            enabled = !state.actionLoading
                        )

                        Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))
                    }

                    if (state.isAppleAvailable) {
                        CoreButton(
                            text = stringResource(Res.string.unlock_by_apple),
                            onClick = component::onSelectAppleUnlock,
                            modifier = Modifier.testTag(UnlockMethodSelectionTestTags.UNLOCK_APPLE_BUTTON),
                            enabled = !state.actionLoading
                        )
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
            CoreErrorText(
                text = it.toLocalizedMessage(),
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
    const val LOCKOUT_INFO_TEXT = "UnlockMethodSelection_LockoutInfoText"
    const val UNLOCK_EMAIL_BUTTON = "UnlockMethodSelection_UnlockEmailButton"
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
        "Temporary Lockout" to UnlockMethodSelectionScreenState(
            lockoutType = AccountLockoutType.TEMPORARY,
            lockoutUntil = 1758452400000L
        ),
        "Indefinite Lockout" to UnlockMethodSelectionScreenState(
            lockoutType = AccountLockoutType.INDEFINITE
        ),
        "Action Loading" to UnlockMethodSelectionScreenState(
            actionLoading = true
        ),
        "Action Error" to UnlockMethodSelectionScreenState(
            actionError = CommonError.Unknown()
        ),
        "Email Only Identifier" to UnlockMethodSelectionScreenState(
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

@InternalApi
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

private val defaultPreviewState = UnlockMethodSelectionScreenState()

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(UnlockMethodSelectionPreviewProvider::class) state: UnlockMethodSelectionScreenState
) {
    ScreenPreviewContainer {
        UnlockMethodSelectionScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun AdaptivePreview() {
    ScreenPreviewContainer {
        UnlockMethodSelectionScreenPreviewContent(state = defaultPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        UnlockMethodSelectionScreenPreviewContent(state = defaultPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        UnlockMethodSelectionScreenPreviewContent(state = defaultPreviewState)
    }
}
