package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreEmailTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreOutlinedTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.enter_phone_number
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.target.UnlockTargetInputComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod
import io.github.mudrichenkoevgeny.kmp.feature.user.phone_number
import io.github.mudrichenkoevgeny.kmp.feature.user.send_code
import io.github.mudrichenkoevgeny.kmp.feature.user.unlock_by_email
import io.github.mudrichenkoevgeny.kmp.feature.user.unlock_by_phone
import io.github.mudrichenkoevgeny.kmp.feature.user.unlock_email_input_desc
import io.github.mudrichenkoevgeny.kmp.feature.user.unlock_phone_input_desc
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnlockTargetInputScreen(component: UnlockTargetInputComponent) {
    val state by component.state.subscribeAsState()

    val titleRes = when (state.method) {
        UnlockMethod.EMAIL -> Res.string.unlock_by_email
        UnlockMethod.PHONE -> Res.string.unlock_by_phone
    }

    val descRes = when (state.method) {
        UnlockMethod.EMAIL -> Res.string.unlock_email_input_desc
        UnlockMethod.PHONE -> Res.string.unlock_phone_input_desc
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(titleRes),
                        modifier = Modifier.testTag(UnlockTargetInputTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(UnlockTargetInputTestTags.BACK_BUTTON)
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
                    CoreBodyText(
                        text = stringResource(descRes),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(bottom = CoreTheme.dimens.paddingLarge)
                            .testTag(UnlockTargetInputTestTags.DESC_TEXT)
                    )

                    when (state.method) {
                        UnlockMethod.EMAIL -> {
                            CoreEmailTextField(
                                value = state.input,
                                onValueChange = component::onInputChanged,
                                modifier = Modifier.testTag(UnlockTargetInputTestTags.INPUT),
                                isError = state.actionError != null,
                                enabled = !state.actionLoading
                            )
                        }
                        UnlockMethod.PHONE -> {
                            CoreOutlinedTextField(
                                value = state.input,
                                onValueChange = component::onInputChanged,
                                label = { CoreBodyText(stringResource(Res.string.phone_number)) },
                                placeholder = { CoreBodyText(stringResource(Res.string.enter_phone_number)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.testTag(UnlockTargetInputTestTags.INPUT),
                                isError = state.actionError != null,
                                enabled = !state.actionLoading
                            )
                        }
                    }

                    ErrorText(state.actionError)
                }

                CoreButton(
                    text = stringResource(Res.string.send_code),
                    onClick = component::onSendCodeClick,
                    modifier = Modifier.testTag(UnlockTargetInputTestTags.SUBMIT_BUTTON),
                    enabled = state.canSendCode
                )
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
                    .testTag(UnlockTargetInputTestTags.ACTION_ERROR_TEXT)
            )
        }
    }
}

internal object UnlockTargetInputTestTags {
    const val TITLE = "UnlockTargetInput_Title"
    const val BACK_BUTTON = "UnlockTargetInput_BackButton"
    const val DESC_TEXT = "UnlockTargetInput_DescText"
    const val INPUT = "UnlockTargetInput_Input"
    const val SUBMIT_BUTTON = "UnlockTargetInput_SubmitButton"
    const val ACTION_ERROR_TEXT = "UnlockTargetInput_ActionErrorText"
}

@InternalApi
internal class UnlockTargetInputPreviewProvider :
    PreviewParameterProvider<UnlockTargetInputScreenState> {

    private val items: List<Pair<String, UnlockTargetInputScreenState>> = listOf(
        "Email Input" to UnlockTargetInputScreenState(
            method = UnlockMethod.EMAIL,
            input = "user@example.com"
        ),
        "Phone Input" to UnlockTargetInputScreenState(
            method = UnlockMethod.PHONE,
            input = "+79991234567"
        ),
        "Action Loading" to UnlockTargetInputScreenState(
            method = UnlockMethod.EMAIL,
            input = "user@example.com",
            actionLoading = true
        ),
        "Action Error" to UnlockTargetInputScreenState(
            method = UnlockMethod.EMAIL,
            input = "user@example.com",
            actionError = CommonError.Unknown()
        )
    )

    override val values: Sequence<UnlockTargetInputScreenState> =
        items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? =
        items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun UnlockTargetInputScreenPreviewContent(state: UnlockTargetInputScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        Surface {
            UnlockTargetInputScreen(
                component = UnlockTargetInputComponentMock(initialState = state)
            )
        }
    }
}

private val defaultPreviewState = UnlockTargetInputScreenState(
    method = UnlockMethod.EMAIL,
    input = "user@example.com"
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(UnlockTargetInputPreviewProvider::class) state: UnlockTargetInputScreenState
) {
    ScreenPreviewContainer {
        UnlockTargetInputScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun AdaptivePreview() {
    ScreenPreviewContainer {
        UnlockTargetInputScreenPreviewContent(state = defaultPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        UnlockTargetInputScreenPreviewContent(state = defaultPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        UnlockTargetInputScreenPreviewContent(state = defaultPreviewState)
    }
}
