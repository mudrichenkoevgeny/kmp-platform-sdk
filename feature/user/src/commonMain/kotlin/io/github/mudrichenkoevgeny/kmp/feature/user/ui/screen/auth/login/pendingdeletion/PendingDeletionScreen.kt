package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenOverlayLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.DialogPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.DialogSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.account_pending_deletion_desc
import io.github.mudrichenkoevgeny.kmp.feature.user.account_pending_deletion_title
import io.github.mudrichenkoevgeny.kmp.feature.user.logout
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.pendingdeletion.PendingDeletionComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.restore_account
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingDeletionScreen(component: PendingDeletionComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.account_pending_deletion_title),
                        modifier = Modifier.testTag(PendingDeletionTestTags.TITLE)
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(CoreTheme.dimens.paddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(CoreTheme.dimens.paddingMedium),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CoreTitleText(
                                text = stringResource(Res.string.account_pending_deletion_title),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.testTag(PendingDeletionTestTags.CARD_TITLE)
                            )
                            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                            CoreBodyText(
                                text = stringResource(Res.string.account_pending_deletion_desc),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.testTag(PendingDeletionTestTags.CARD_DESC)
                            )
                        }
                    }

                    ErrorText(
                        error = state.actionError,
                        testTag = PendingDeletionTestTags.ACTION_ERROR_TEXT
                    )
                }

                CoreButton(
                    text = stringResource(Res.string.restore_account),
                    onClick = component::onRestoreAccountClick,
                    modifier = Modifier.testTag(PendingDeletionTestTags.RESTORE_BUTTON),
                    enabled = !state.actionLoading
                )

                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))

                CoreTextButton(
                    text = stringResource(Res.string.logout),
                    onClick = component::onSignOutClick,
                    modifier = Modifier.testTag(PendingDeletionTestTags.SIGN_OUT_BUTTON),
                    enabled = !state.actionLoading
                )
            }

            if (state.actionLoading) {
                FullscreenOverlayLoading()
            }
        }
    }
}

@Composable
private fun ErrorText(error: AppError?, testTag: String) {
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
                    .testTag(testTag)
            )
        }
    }
}

@InternalApi
internal class PendingDeletionPreviewProvider : PreviewParameterProvider<PendingDeletionScreenState> {
    private val items: List<Pair<String, PendingDeletionScreenState>> = listOf(
        "Default Content" to PendingDeletionScreenState(),
        "Action Loading" to PendingDeletionScreenState(actionLoading = true),
        "Inline Error" to PendingDeletionScreenState(actionError = CommonError.Unknown())
    )

    override val values: Sequence<PendingDeletionScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun PendingDeletionScreenPreviewContent(state: PendingDeletionScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        PendingDeletionScreen(
            component = PendingDeletionComponentMock(initialState = state)
        )
    }
}

private val defaultPendingDeletionPreviewState = PendingDeletionScreenState()

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(PendingDeletionPreviewProvider::class) state: PendingDeletionScreenState
) {
    DialogPreviewContainer {
        PendingDeletionScreenPreviewContent(state = state)
    }
}

@InternalApi
@DialogSizePreviews
@Composable
private fun DialogSizePreview() {
    DialogPreviewContainer {
        PendingDeletionScreenPreviewContent(state = defaultPendingDeletionPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    DialogPreviewContainer {
        PendingDeletionScreenPreviewContent(state = defaultPendingDeletionPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    DialogPreviewContainer {
        PendingDeletionScreenPreviewContent(state = defaultPendingDeletionPreviewState)
    }
}

object PendingDeletionTestTags {
    const val TITLE = "PendingDeletion_Title"
    const val CARD_TITLE = "PendingDeletion_CardTitle"
    const val CARD_DESC = "PendingDeletion_CardDesc"
    const val RESTORE_BUTTON = "PendingDeletion_RestoreButton"
    const val SIGN_OUT_BUTTON = "PendingDeletion_SignOutButton"
    const val ACTION_ERROR_TEXT = "PendingDeletion_ActionErrorText"
}
