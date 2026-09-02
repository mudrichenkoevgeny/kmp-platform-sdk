package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenOverlayLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.account_pending_deletion_desc
import io.github.mudrichenkoevgeny.kmp.feature.user.account_pending_deletion_title
import io.github.mudrichenkoevgeny.kmp.feature.user.logout
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
                    Text(
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
                    .fillMaxWidth()
                    .padding(Dimens.paddingLarge),
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
                            .padding(Dimens.paddingMedium),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(Res.string.account_pending_deletion_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.testTag(PendingDeletionTestTags.CARD_TITLE)
                        )
                        Spacer(Modifier.height(Dimens.paddingSmall))
                        Text(
                            text = stringResource(Res.string.account_pending_deletion_desc),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.testTag(PendingDeletionTestTags.CARD_DESC)
                        )
                    }
                }

                Spacer(Modifier.height(Dimens.paddingLarge))

                Button(
                    onClick = component::onRestoreAccountClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(PendingDeletionTestTags.RESTORE_BUTTON),
                    enabled = !state.actionLoading
                ) {
                    Text(text = stringResource(Res.string.restore_account))
                }

                Spacer(Modifier.height(Dimens.paddingSmall))

                OutlinedButton(
                    onClick = component::onSignOutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(PendingDeletionTestTags.SIGN_OUT_BUTTON),
                    enabled = !state.actionLoading
                ) {
                    Text(text = stringResource(Res.string.logout))
                }

                ErrorText(
                    error = state.actionError,
                    testTag = PendingDeletionTestTags.ACTION_ERROR_TEXT
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
            Text(
                text = it.toLocalizedMessage(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = Dimens.paddingSmall)
                    .testTag(testTag)
            )
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun PendingDeletionScreenPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                PendingDeletionScreen(
                    component = object : PendingDeletionComponent {
                        override val state = com.arkivanov.decompose.value.MutableValue(PendingDeletionScreenState())
                        override fun onRestoreAccountClick() {}
                        override fun onSignOutClick() {}
                    }
                )
            }
        }
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
