package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.row.CoreDetailRow
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.preview.StorageFileImagePreview
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.container.CoreScrollableScreenContent
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error.FullscreenError
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
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.file.detail.StorageFileDetailComponentMock
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.preview.StorageFilePreview
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.detail.dialog.StorageFileDetailDialogChild
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.utils.StorageStringsMapper
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.status.StorageFileStatus
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.file.createTestStorageFilePrivate

/**
 * Screen displaying detailed information about a single storage file.
 *
 * @param component Decompose controller driving state and callbacks.
 * @param modifier Optional [Modifier] for screen positioning.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageFileDetailScreen(
    component: StorageFileDetailComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.subscribeAsState()
    val storageUi = LocalStorageStrings.current.ui
    val commonUi = LocalCommonStrings.current.ui
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = storageUi.fileDetailTitle,
                        modifier = Modifier.testTag(StorageFileDetailTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(StorageFileDetailTestTags.BACK_BUTTON)
                    )
                }
            )
        }
    ) { padding ->
        when (val currentState = state) {
            is StorageFileDetailScreenState.Loading -> {
                FullscreenLoading(modifier = Modifier.padding(padding))
            }
            is StorageFileDetailScreenState.Error -> {
                FullscreenError(
                    error = currentState.error,
                    onRetry = component::onRetry,
                    modifier = Modifier.padding(padding)
                )
            }
            is StorageFileDetailScreenState.Content -> {
                StorageFileDetailContent(
                    state = currentState,
                    isDeleteAvailable = component.isDeleteAvailable,
                    onCopyUrlClick = {
                        clipboardManager.setText(AnnotatedString(currentState.file.fileUrl))
                        coroutineScope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(message = storageUi.urlCopied)
                        }
                    },
                    onDeleteClick = component::onDeleteClick,
                    onUserClick = component::onUserClick,
                    modifier = Modifier.padding(padding)
                )

                val dialogSlot by component.dialogSlot.subscribeAsState()
                val dialogChild = dialogSlot.child?.instance
                if (dialogChild != null) {
                    when (dialogChild) {
                        is StorageFileDetailDialogChild.ConfirmDeleteFile -> {
                            AlertDialog(
                                onDismissRequest = component::onDismissDeleteDialog,
                                title = { CoreTitleText(text = storageUi.deleteFile) },
                                text = { CoreBodyText(text = storageUi.deleteFileConfirm) },
                                confirmButton = {
                                    CoreTextButton(
                                        text = storageUi.deleteFileAction,
                                        onClick = component::onConfirmDelete
                                    )
                                },
                                dismissButton = {
                                    CoreTextButton(
                                        text = commonUi.no,
                                        onClick = component::onDismissDeleteDialog
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
private fun StorageFileDetailContent(
    state: StorageFileDetailScreenState.Content,
    isDeleteAvailable: Boolean,
    onCopyUrlClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onUserClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val storageUi = LocalStorageStrings.current.ui
    val commonUi = LocalCommonStrings.current.ui
    val file = state.file
    var isImagePreviewVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Delayed reveal of heavy Coil component to prevent navigation transition jank
        delay(150.milliseconds)
        isImagePreviewVisible = true
    }

    CoreScrollableScreenContent(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(CoreTheme.dimens.previewContainerHeight),
            contentAlignment = Alignment.Center
        ) {
            if (isImagePreviewVisible && file.category == StorageCategory.IMAGE && file.fileUrl.isNotBlank()) {
                StorageFileImagePreview(
                    imageUrl = file.fileUrl,
                    category = file.category,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                StorageFilePreview(
                    category = file.category,
                    containerSize = CoreTheme.dimens.previewContainerHeight,
                    iconSize = CoreTheme.dimens.iconSizeXLarge
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CoreTheme.dimens.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
            ) {
                CoreDetailRow(label = storageUi.fileName, value = file.fileName)

                file.ownerUserId?.let { ownerUserId ->
                    CoreDetailRow(
                        label = storageUi.ownerUserId,
                        value = ownerUserId,
                        onClick = onUserClick,
                        modifier = Modifier.testTag(StorageFileDetailTestTags.USER_ROW)
                    )
                }

                CoreDetailRow(label = storageUi.fileSize, value = file.fileSizeBytes.toString())
                CoreDetailRow(label = storageUi.mimeType, value = file.mimeType)
                CoreDetailRow(
                    label = storageUi.category,
                    value = StorageStringsMapper.getCategoryString(file.category, storageUi)
                )
                CoreDetailRow(label = storageUi.visibility, value = file.visibility.name)
                CoreDetailRow(label = commonUi.status, value = file.status.name)
            }
        }

        CoreErrorText(
            text = state.deleteError?.toLocalizedMessage(),
            animated = true,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = CoreTheme.dimens.paddingSmall)
                .testTag(StorageFileDetailTestTags.DELETE_ERROR_TEXT)
        )

        CoreButton(
            text = storageUi.copyFileUrl,
            onClick = onCopyUrlClick,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(StorageFileDetailTestTags.COPY_URL_BUTTON)
        )

        if (file.status != StorageFileStatus.DELETED && isDeleteAvailable) {
            CoreButton(
                text = storageUi.deleteFileAction,
                onClick = onDeleteClick,
                enabled = !state.isDeleting,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(StorageFileDetailTestTags.DELETE_BUTTON)
            )
        }
    }
}

private data class StorageFileDetailPreviewState(
    val state: StorageFileDetailScreenState = StorageFileDetailScreenState.Content(
        file = createTestStorageFilePrivate()
    )
)

@InternalApi
private class StorageFileDetailPreviewProvider : PreviewParameterProvider<StorageFileDetailPreviewState> {
    private val items: List<Pair<String, StorageFileDetailPreviewState>> = listOf(
        "Content" to StorageFileDetailPreviewState(),
        "Loading" to StorageFileDetailPreviewState(state = StorageFileDetailScreenState.Loading),
        "Error" to StorageFileDetailPreviewState(
            state = StorageFileDetailScreenState.Error(error = CommonError.Unknown())
        )
    )

    override val values: Sequence<StorageFileDetailPreviewState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun StorageFileDetailPreviewContent(state: StorageFileDetailPreviewState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        ScreenPreviewContainer {
            CoreTheme {
                Surface {
                    Box {
                        StorageFileDetailScreen(
                            component = StorageFileDetailComponentMock(
                                initialState = state.state
                            )
                        )
                    }
                }
            }
        }
    }
}

@InternalApi
private val defaultStorageFileDetailPreviewState = StorageFileDetailPreviewState()

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StorageFileDetailStatesPreview(
    @PreviewParameter(StorageFileDetailPreviewProvider::class) state: StorageFileDetailPreviewState
) {
    StorageFileDetailPreviewContent(state = state)
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun StorageFileDetailComponentSizePreview() {
    StorageFileDetailPreviewContent(state = defaultStorageFileDetailPreviewState)
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun StorageFileDetailScreenSizePreview() {
    StorageFileDetailPreviewContent(state = defaultStorageFileDetailPreviewState)
}

@InternalApi
@ThemePreviews
@Composable
private fun StorageFileDetailThemePreview() {
    StorageFileDetailPreviewContent(state = defaultStorageFileDetailPreviewState)
}

@InternalApi
@FontScalePreviews
@Composable
private fun StorageFileDetailFontScalePreview() {
    StorageFileDetailPreviewContent(state = defaultStorageFileDetailPreviewState)
}
