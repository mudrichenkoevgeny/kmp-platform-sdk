package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.mfa

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreOutlinedTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.DialogPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.DialogSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.cancel
import io.github.mudrichenkoevgeny.kmp.feature.user.confirm
import io.github.mudrichenkoevgeny.kmp.feature.user.mfa_step_up_desc
import io.github.mudrichenkoevgeny.kmp.feature.user.mfa_step_up_title
import io.github.mudrichenkoevgeny.kmp.feature.user.network.httpclient.mfa.MfaChallengeRequest
import io.github.mudrichenkoevgeny.kmp.feature.user.totp_code
import kotlinx.coroutines.CompletableDeferred
import org.jetbrains.compose.resources.stringResource

/**
 * Renders a step-up authentication dialog prompting the user for a TOTP code.
 *
 * @param request The active [MfaChallengeRequest].
 * @param onConfirm Invoked when the user confirms with their code.
 * @param onCancel Invoked when the user dismisses the challenge.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MfaChallengeDialog(
    request: MfaChallengeRequest,
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit
) {
    var codeInput by remember(request.mfaToken) { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            CoreTitleText(
                text = stringResource(Res.string.mfa_step_up_title),
                modifier = Modifier.testTag(MfaChallengeDialogTestTags.TITLE)
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                CoreBodyText(
                    text = stringResource(Res.string.mfa_step_up_desc),
                    modifier = Modifier.testTag(MfaChallengeDialogTestTags.DESC)
                )

                Spacer(Modifier.height(CoreTheme.dimens.paddingMedium))

                CoreOutlinedTextField(
                    value = codeInput,
                    onValueChange = { codeInput = it },
                    label = { CoreBodyText(stringResource(Res.string.totp_code)) },
                    placeholder = { CoreBodyText(stringResource(Res.string.totp_code)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(MfaChallengeDialogTestTags.CODE_INPUT)
                )
            }
        },
        confirmButton = {
            CoreTextButton(
                text = stringResource(Res.string.confirm),
                onClick = { onConfirm(codeInput) },
                enabled = codeInput.isNotBlank(),
                modifier = Modifier.testTag(MfaChallengeDialogTestTags.CONFIRM_BUTTON)
            )
        },
        dismissButton = {
            CoreTextButton(
                text = stringResource(Res.string.cancel),
                onClick = onCancel,
                modifier = Modifier.testTag(MfaChallengeDialogTestTags.CANCEL_BUTTON)
            )
        }
    )
}

@InternalApi
@Composable
private fun MfaChallengeDialogPreviewContent() {
    val request = MfaChallengeRequest(
        mfaToken = "mock_mfa_token",
        deferred = CompletableDeferred()
    )
    DialogPreviewContainer {
        MfaChallengeDialog(
            request = request,
            onConfirm = {},
            onCancel = {}
        )
    }
}

@InternalApi
@DialogSizePreviews
@Composable
private fun DialogSizePreview() {
    MfaChallengeDialogPreviewContent()
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    MfaChallengeDialogPreviewContent()
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    MfaChallengeDialogPreviewContent()
}

object MfaChallengeDialogTestTags {
    const val TITLE = "MfaChallengeDialog_Title"
    const val DESC = "MfaChallengeDialog_Desc"
    const val CODE_INPUT = "MfaChallengeDialog_CodeInput"
    const val CONFIRM_BUTTON = "MfaChallengeDialog_ConfirmButton"
    const val CANCEL_BUTTON = "MfaChallengeDialog_CancelButton"
}
