package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
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
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.container.CoreScrollableScreenContent
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error.FullscreenError
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input.CoreOutlinedTextField
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.storage.localization.LocalStorageStrings
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.settings.EditStorageSettingsComponentMock
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.settings.dialog.StorageSettingsDialogChild
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.storagesettings.createTestManagementStorageSettings

/**
 * Screen for viewing and editing platform storage settings.
 *
 * @param component Decompose controller driving state and callbacks.
 * @param modifier Optional [Modifier] for screen positioning.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStorageSettingsScreen(
    component: EditStorageSettingsComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.subscribeAsState()
    val storageUi = LocalStorageStrings.current.ui
    val commonUi = LocalCommonStrings.current.ui

    val contentState = state as? EditStorageSettingsScreenState.Content

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = storageUi.storageSettingsTitle,
                        modifier = Modifier.testTag(EditStorageSettingsTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        enabled = contentState?.isSaving != true,
                        modifier = Modifier.testTag(EditStorageSettingsTestTags.BACK_BUTTON)
                    )
                }
            )
        }
    ) { padding ->
        when (val currentState = state) {
            is EditStorageSettingsScreenState.Loading -> {
                FullscreenLoading(modifier = Modifier.padding(padding))
            }
            is EditStorageSettingsScreenState.Error -> {
                FullscreenError(
                    error = currentState.error,
                    onRetry = component::onRetry,
                    modifier = Modifier.padding(padding)
                )
            }
            is EditStorageSettingsScreenState.Content -> {
                EditStorageSettingsForm(
                    state = currentState,
                    onMinUploadSizeBytesChanged = component::onMinUploadSizeBytesChanged,
                    onMaxUploadSizeBytesChanged = component::onMaxUploadSizeBytesChanged,
                    onTotalStorageQuotaForOpenUserBytesChanged = component::onTotalStorageQuotaForOpenUserBytesChanged,
                    onTotalStorageQuotaForManagementUserBytesChanged =
                    component::onTotalStorageQuotaForManagementUserBytesChanged,
                    onPresignedUrlExpirationSecondsChanged = component::onPresignedUrlExpirationSecondsChanged,
                    onMaxPendingUploadsForOpenUserChanged = component::onMaxPendingUploadsForOpenUserChanged,
                    onMaxPendingUploadsForManagementUserChanged = component::onMaxPendingUploadsForManagementUserChanged,
                    onIsDeduplicationEnabledToggled = component::onIsDeduplicationEnabledToggled,
                    onDeletedFilesCleanupIntervalSecondsChanged = component::onDeletedFilesCleanupIntervalSecondsChanged,
                    onSaveClick = component::onSaveClick,
                    onResetClick = component::onResetClick,
                    modifier = Modifier.padding(padding)
                )

                val dialogSlot by component.dialogSlot.subscribeAsState()
                val dialogChild = dialogSlot.child?.instance
                if (dialogChild != null) {
                    when (dialogChild) {
                        is StorageSettingsDialogChild.ConfirmReset -> {
                            AlertDialog(
                                onDismissRequest = component::onDismissDialog,
                                title = { CoreTitleText(text = storageUi.storageSettingsTitle) },
                                text = { CoreBodyText(text = commonUi.reset) },
                                confirmButton = {
                                    CoreTextButton(
                                        text = commonUi.yes,
                                        onClick = component::onConfirmReset
                                    )
                                },
                                dismissButton = {
                                    CoreTextButton(
                                        text = commonUi.no,
                                        onClick = component::onDismissDialog
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditStorageSettingsForm(
    state: EditStorageSettingsScreenState.Content,
    onMinUploadSizeBytesChanged: (String) -> Unit,
    onMaxUploadSizeBytesChanged: (String) -> Unit,
    onTotalStorageQuotaForOpenUserBytesChanged: (String) -> Unit,
    onTotalStorageQuotaForManagementUserBytesChanged: (String) -> Unit,
    onPresignedUrlExpirationSecondsChanged: (String) -> Unit,
    onMaxPendingUploadsForOpenUserChanged: (String) -> Unit,
    onMaxPendingUploadsForManagementUserChanged: (String) -> Unit,
    onIsDeduplicationEnabledToggled: (Boolean) -> Unit,
    onDeletedFilesCleanupIntervalSecondsChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val storageUi = LocalStorageStrings.current.ui
    val commonUi = LocalCommonStrings.current.ui

    CoreScrollableScreenContent(
        modifier = modifier
    ) {
        CoreOutlinedTextField(
            value = state.minUploadSizeBytes,
            onValueChange = onMinUploadSizeBytesChanged,
            label = { CoreBodyText(storageUi.minUploadSizeBytes) },
            enabled = !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditStorageSettingsTestTags.MIN_UPLOAD_SIZE_BYTES_INPUT)
        )

        CoreOutlinedTextField(
            value = state.maxUploadSizeBytes,
            onValueChange = onMaxUploadSizeBytesChanged,
            label = { CoreBodyText(storageUi.maxUploadSizeBytes) },
            enabled = !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditStorageSettingsTestTags.MAX_UPLOAD_SIZE_BYTES_INPUT)
        )

        CoreOutlinedTextField(
            value = state.totalStorageQuotaForOpenUserBytes,
            onValueChange = onTotalStorageQuotaForOpenUserBytesChanged,
            label = { CoreBodyText(storageUi.totalStorageQuotaForOpenUserBytes) },
            enabled = !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditStorageSettingsTestTags.TOTAL_STORAGE_QUOTA_FOR_OPEN_USER_BYTES_INPUT)
        )

        CoreOutlinedTextField(
            value = state.totalStorageQuotaForManagementUserBytes,
            onValueChange = onTotalStorageQuotaForManagementUserBytesChanged,
            label = { CoreBodyText(storageUi.totalStorageQuotaForManagementUserBytes) },
            enabled = !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditStorageSettingsTestTags.TOTAL_STORAGE_QUOTA_FOR_MANAGEMENT_USER_BYTES_INPUT)
        )

        CoreOutlinedTextField(
            value = state.presignedUrlExpirationSeconds,
            onValueChange = onPresignedUrlExpirationSecondsChanged,
            label = { CoreBodyText(storageUi.presignedUrlExpirationSeconds) },
            enabled = !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditStorageSettingsTestTags.PRESIGNED_URL_EXPIRATION_SECONDS_INPUT)
        )

        CoreOutlinedTextField(
            value = state.maxPendingUploadsForOpenUser,
            onValueChange = onMaxPendingUploadsForOpenUserChanged,
            label = { CoreBodyText(storageUi.maxPendingUploadsForOpenUser) },
            enabled = !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditStorageSettingsTestTags.MAX_PENDING_UPLOADS_FOR_OPEN_USER_INPUT)
        )

        CoreOutlinedTextField(
            value = state.maxPendingUploadsForManagementUser,
            onValueChange = onMaxPendingUploadsForManagementUserChanged,
            label = { CoreBodyText(storageUi.maxPendingUploadsForManagementUser) },
            enabled = !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditStorageSettingsTestTags.MAX_PENDING_UPLOADS_FOR_MANAGEMENT_USER_INPUT)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
        ) {
            Checkbox(
                checked = state.isDeduplicationEnabled,
                onCheckedChange = onIsDeduplicationEnabledToggled,
                enabled = !state.isSaving,
                modifier = Modifier.testTag(EditStorageSettingsTestTags.IS_DEDUPLICATION_ENABLED_CHECKBOX)
            )
            CoreBodyText(text = storageUi.isDeduplicationEnabled)
        }

        CoreOutlinedTextField(
            value = state.deletedFilesCleanupIntervalSeconds,
            onValueChange = onDeletedFilesCleanupIntervalSecondsChanged,
            label = { CoreBodyText(storageUi.deletedFilesCleanupIntervalSeconds) },
            enabled = !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditStorageSettingsTestTags.DELETED_FILES_CLEANUP_INTERVAL_SECONDS_INPUT)
        )

        ErrorText(
            error = state.saveError,
            testTag = EditStorageSettingsTestTags.ERROR_TEXT
        )

        CoreButton(
            text = if (state.isSaving) commonUi.loading else commonUi.save,
            onClick = onSaveClick,
            enabled = state.hasChanges && !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditStorageSettingsTestTags.SAVE_BUTTON)
        )

        CoreTextButton(
            text = commonUi.reset,
            onClick = onResetClick,
            enabled = !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(EditStorageSettingsTestTags.RESET_BUTTON)
        )
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
                    .fillMaxWidth()
                    .padding(top = CoreTheme.dimens.paddingSmall)
                    .testTag(testTag)
            )
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun ErrorTextPreview() {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        CoreTheme {
            Surface {
                ErrorText(
                    error = CommonError.Unknown(),
                    testTag = EditStorageSettingsTestTags.ERROR_TEXT
                )
            }
        }
    }
}

private data class EditStorageSettingsPreviewState(
    val state: EditStorageSettingsScreenState = EditStorageSettingsScreenState.Content(
        initialSettings = createTestManagementStorageSettings(),
        minUploadSizeBytes = "1024",
        maxUploadSizeBytes = "104857600",
        totalStorageQuotaForOpenUserBytes = "1073741824",
        totalStorageQuotaForManagementUserBytes = "10737418240",
        presignedUrlExpirationSeconds = "3600",
        maxPendingUploadsForOpenUser = "10",
        maxPendingUploadsForManagementUser = "50",
        isDeduplicationEnabled = true,
        deletedFilesCleanupIntervalSeconds = "86400"
    )
)

@InternalApi
private class EditStorageSettingsPreviewProvider : PreviewParameterProvider<EditStorageSettingsPreviewState> {
    private val items: List<Pair<String, EditStorageSettingsPreviewState>> = listOf(
        "Content" to EditStorageSettingsPreviewState(),
        "Loading" to EditStorageSettingsPreviewState(state = EditStorageSettingsScreenState.Loading),
        "Error" to EditStorageSettingsPreviewState(
            state = EditStorageSettingsScreenState.Error(error = CommonError.Unknown())
        )
    )

    override val values: Sequence<EditStorageSettingsPreviewState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun EditStorageSettingsPreviewContent(state: EditStorageSettingsPreviewState) {
    ScreenPreviewContainer {
        CoreTheme {
            Surface {
                Box {
                    EditStorageSettingsScreen(
                        component = EditStorageSettingsComponentMock(
                            initialState = state.state
                        )
                    )
                }
            }
        }
    }
}

@InternalApi
private val defaultEditStorageSettingsPreviewState = EditStorageSettingsPreviewState()

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun EditStorageSettingsStatesPreview(
    @PreviewParameter(EditStorageSettingsPreviewProvider::class) state: EditStorageSettingsPreviewState
) {
    EditStorageSettingsPreviewContent(state = state)
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun EditStorageSettingsComponentSizePreview() {
    EditStorageSettingsPreviewContent(state = defaultEditStorageSettingsPreviewState)
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun EditStorageSettingsScreenSizePreview() {
    EditStorageSettingsPreviewContent(state = defaultEditStorageSettingsPreviewState)
}

@InternalApi
@ThemePreviews
@Composable
private fun EditStorageSettingsThemePreview() {
    EditStorageSettingsPreviewContent(state = defaultEditStorageSettingsPreviewState)
}

@InternalApi
@FontScalePreviews
@Composable
private fun EditStorageSettingsFontScalePreview() {
    EditStorageSettingsPreviewContent(state = defaultEditStorageSettingsPreviewState)
}
