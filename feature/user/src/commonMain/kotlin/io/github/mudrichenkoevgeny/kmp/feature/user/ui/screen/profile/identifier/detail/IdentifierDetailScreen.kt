package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.time.formatEpochMillisToDateTime
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.container.CoreScrollableScreenContent
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error.FullscreenError
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreSmallText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.cancel
import io.github.mudrichenkoevgeny.kmp.feature.user.change_password
import io.github.mudrichenkoevgeny.kmp.feature.user.confirm
import io.github.mudrichenkoevgeny.kmp.feature.user.dialog_cancel
import io.github.mudrichenkoevgeny.kmp.feature.user.dialog_confirm
import io.github.mudrichenkoevgeny.kmp.feature.user.dialog_confirm_title
import io.github.mudrichenkoevgeny.kmp.feature.user.identifier_delete_button
import io.github.mudrichenkoevgeny.kmp.feature.user.identifier_delete_password_button
import io.github.mudrichenkoevgeny.kmp.feature.user.identifier_detail_auth_provider
import io.github.mudrichenkoevgeny.kmp.feature.user.identifier_detail_created_at
import io.github.mudrichenkoevgeny.kmp.feature.user.identifier_detail_id
import io.github.mudrichenkoevgeny.kmp.feature.user.identifier_detail_title
import io.github.mudrichenkoevgeny.kmp.feature.user.identifier_detail_title_current
import io.github.mudrichenkoevgeny.kmp.feature.user.identifier_detail_updated_at
import io.github.mudrichenkoevgeny.kmp.feature.user.identifier_detail_value
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CorePasswordTextField
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.identifier.detail.IdentifierDetailComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.new_password
import io.github.mudrichenkoevgeny.kmp.feature.user.not_available
import io.github.mudrichenkoevgeny.kmp.feature.user.old_password
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdentifierDetailScreen(component: IdentifierDetailComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val currentContent = state as? IdentifierDetailScreenState.Content
                    val titleRes = if (currentContent?.isCurrentIdentifier == true) {
                        Res.string.identifier_detail_title_current
                    } else {
                        Res.string.identifier_detail_title
                    }
                    CoreScreenTitleText(
                        text = stringResource(titleRes),
                        modifier = Modifier.testTag(IdentifierDetailTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(IdentifierDetailTestTags.BACK_BUTTON)
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
            when (val currentState = state) {
                is IdentifierDetailScreenState.Loading -> FullscreenLoading()
                is IdentifierDetailScreenState.Error -> FullscreenError(
                    error = currentState.error,
                    onRetry = component::onRetry,
                    modifier = Modifier.testTag(IdentifierDetailTestTags.GLOBAL_ERROR)
                )
                is IdentifierDetailScreenState.Content -> {
                    Content(
                        state = currentState,
                        onDeleteIdentifierRequested = component::onDeleteIdentifierRequested,
                        onDismissDeleteIdentifierDialog = component::onDismissDeleteIdentifierDialog,
                        onDeleteIdentifierConfirm = component::onDeleteIdentifierClick,
                        onChangePasswordClick = component::onChangePasswordClick,
                        onConfirmChangePasswordClick = component::onConfirmChangePasswordClick,
                        onDismissChangePasswordDialog = component::onDismissChangePasswordDialog,
                        onDeletePasswordClick = component::onDeletePasswordClick
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: IdentifierDetailScreenState.Content,
    onDeleteIdentifierRequested: () -> Unit,
    onDismissDeleteIdentifierDialog: () -> Unit,
    onDeleteIdentifierConfirm: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onConfirmChangePasswordClick: (String, String) -> Unit,
    onDismissChangePasswordDialog: () -> Unit,
    onDeletePasswordClick: () -> Unit
) {
    val identifier = state.identifier

    CoreScrollableScreenContent {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(IdentifierDetailTestTags.CARD),
            elevation = CardDefaults.cardElevation(defaultElevation = CoreTheme.dimens.elevationHeader)
        ) {
            Column(
                modifier = Modifier.padding(CoreTheme.dimens.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
            ) {
                CoreTitleText(
                    text = identifier.displayName,
                    style = MaterialTheme.typography.titleLarge
                )

                HorizontalDivider()

                DetailRow(
                    label = stringResource(Res.string.identifier_detail_value),
                    value = identifier.identifier
                )

                DetailRow(
                    label = stringResource(Res.string.identifier_detail_id),
                    value = identifier.id.asHexDashString()
                )

                DetailRow(
                    label = stringResource(Res.string.identifier_detail_auth_provider),
                    value = identifier.userAuthProvider.name
                )

                DetailRow(
                    label = "External Email",
                    value = identifier.externalProviderEmail ?: stringResource(Res.string.not_available)
                )

                val createdAtFormatted = formatEpochMillisToDateTime(identifier.createdAt.toEpochMilliseconds())
                    ?: identifier.createdAt.toString()
                DetailRow(
                    label = stringResource(Res.string.identifier_detail_created_at),
                    value = createdAtFormatted
                )

                val updatedAtFormatted = identifier.updatedAt?.let { updatedAt ->
                    formatEpochMillisToDateTime(updatedAt.toEpochMilliseconds()) ?: updatedAt.toString()
                } ?: stringResource(Res.string.not_available)
                DetailRow(
                    label = stringResource(Res.string.identifier_detail_updated_at),
                    value = updatedAtFormatted
                )
            }
        }

        if (state.actionError != null && !state.isChangePasswordDialogVisible) {
            CoreErrorText(
                text = state.actionError.toLocalizedMessage(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (state.canChangePassword) {
            CoreButton(
                text = stringResource(Res.string.change_password),
                onClick = onChangePasswordClick,
                enabled = !state.actionLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(IdentifierDetailTestTags.CHANGE_PASSWORD_BUTTON)
            )
        }

        if (state.canDeletePassword) {
            CoreButton(
                text = stringResource(Res.string.identifier_delete_password_button),
                onClick = onDeletePasswordClick,
                enabled = !state.actionLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(IdentifierDetailTestTags.DELETE_PASSWORD_BUTTON)
            )
        }

        if (!state.isCurrentIdentifier) {
            CoreButton(
                text = stringResource(Res.string.identifier_delete_button),
                onClick = onDeleteIdentifierRequested,
                enabled = !state.actionLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(IdentifierDetailTestTags.DELETE_BUTTON)
            )
        }
    }

    if (state.isChangePasswordDialogVisible) {
        ChangePasswordDialog(
            onConfirm = onConfirmChangePasswordClick,
            onDismiss = onDismissChangePasswordDialog,
            loading = state.actionLoading,
            error = state.actionError
        )
    }

    if (state.isDeleteConfirmationVisible) {
        AlertDialog(
            onDismissRequest = onDismissDeleteIdentifierDialog,
            title = { CoreTitleText(stringResource(Res.string.dialog_confirm_title)) },
            confirmButton = {
                CoreTextButton(
                    text = stringResource(Res.string.dialog_confirm),
                    onClick = onDeleteIdentifierConfirm,
                    enabled = !state.actionLoading
                )
            },
            dismissButton = {
                CoreTextButton(
                    text = stringResource(Res.string.dialog_cancel),
                    onClick = onDismissDeleteIdentifierDialog,
                    enabled = !state.actionLoading
                )
            }
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoreSmallText(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        CoreBodyText(
            text = value
        )
    }
}

@Composable
private fun ChangePasswordDialog(
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit,
    loading: Boolean,
    error: AppError? = null
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var isOldPasswordVisible by remember { mutableStateOf(false) }
    var isNewPasswordVisible by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { CoreTitleText(text = stringResource(Res.string.change_password)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)) {
                CorePasswordTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    isPasswordVisible = isOldPasswordVisible,
                    onTogglePasswordVisibility = { isOldPasswordVisible = !isOldPasswordVisible },
                    label = { CoreBodyText(stringResource(Res.string.old_password)) },
                    placeholder = { CoreBodyText(stringResource(Res.string.old_password)) },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth()
                )
                CorePasswordTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    isPasswordVisible = isNewPasswordVisible,
                    onTogglePasswordVisibility = { isNewPasswordVisible = !isNewPasswordVisible },
                    label = { CoreBodyText(stringResource(Res.string.new_password)) },
                    placeholder = { CoreBodyText(stringResource(Res.string.new_password)) },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth()
                )

                if (error != null) {
                    CoreErrorText(
                        text = error.toLocalizedMessage(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            CoreTextButton(
                text = stringResource(Res.string.confirm),
                onClick = { onConfirm(oldPassword, newPassword) },
                enabled = !loading && oldPassword.isNotBlank() && newPassword.isNotBlank()
            )
        },
        dismissButton = {
            CoreTextButton(
                text = stringResource(Res.string.cancel),
                onClick = onDismiss,
                enabled = !loading
            )
        }
    )
}

@InternalApi
private class IdentifierDetailPreviewProvider : PreviewParameterProvider<IdentifierDetailScreenState> {
    override val values: Sequence<IdentifierDetailScreenState> = sequenceOf(
        IdentifierDetailScreenState.Loading,
        IdentifierDetailScreenState.Error(CommonError.Unknown()),
        IdentifierDetailScreenState.Content(
            identifier = userIdentifierMock(),
            isCurrentIdentifier = true,
            canChangePassword = true,
            canDeletePassword = false
        ),
        IdentifierDetailScreenState.Content(
            identifier = userIdentifierMock(),
            isCurrentIdentifier = false,
            canChangePassword = false,
            canDeletePassword = true
        )
    )
}

@InternalApi
@Composable
private fun IdentifierDetailPreviewContent(state: IdentifierDetailScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        CoreTheme {
            ScreenPreviewContainer {
                IdentifierDetailScreen(component = IdentifierDetailComponentMock(initialState = state))
            }
        }
    }
}

@InternalApi
private val defaultIdentifierDetailPreviewState = IdentifierDetailScreenState.Content(
    identifier = userIdentifierMock(),
    isCurrentIdentifier = false,
    canChangePassword = true,
    canDeletePassword = false
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun IdentifierDetailStatesPreview(
    @PreviewParameter(IdentifierDetailPreviewProvider::class) state: IdentifierDetailScreenState
) {
    IdentifierDetailPreviewContent(state = state)
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun IdentifierDetailScreenSizePreview() {
    IdentifierDetailPreviewContent(state = defaultIdentifierDetailPreviewState)
}

@InternalApi
@ThemePreviews
@Composable
private fun IdentifierDetailThemePreview() {
    IdentifierDetailPreviewContent(state = defaultIdentifierDetailPreviewState)
}

@InternalApi
@FontScalePreviews
@Composable
private fun IdentifierDetailFontScalePreview() {
    IdentifierDetailPreviewContent(state = defaultIdentifierDetailPreviewState)
}

object IdentifierDetailTestTags {
    const val TITLE = "identifier_detail_title"
    const val BACK_BUTTON = "identifier_detail_back_button"
    const val GLOBAL_ERROR = "identifier_detail_global_error"
    const val CARD = "identifier_detail_card"
    const val DELETE_BUTTON = "identifier_detail_delete_button"
    const val CHANGE_PASSWORD_BUTTON = "identifier_detail_change_password_button"
    const val DELETE_PASSWORD_BUTTON = "identifier_detail_delete_password_button"
}
