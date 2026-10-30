package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes
import io.github.mudrichenkoevgeny.kmp.core.common.ic_add
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.storage.localization.LocalStorageStrings
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.component.file.upload.StorageFileUploadComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.ui.component.fileupload.StorageFileUploadUiConfig
import org.jetbrains.compose.resources.painterResource

/**
 * Embedded component providing a dropzone and queue list for multi-file upload.
 *
 * Renders an interactive upload dropzone card and list of queued upload items into the Composition.
 *
 * @param component Presentation controller for managing upload queue state.
 * @param onSelectFilesClick Callback invoked when user taps the dropzone to pick files from device.
 * @param modifier Optional [Modifier] for layout adjustments.
 * @param enabled Whether UI controls and selection are active.
 */
@Composable
fun StorageFileUploadArea(
    component: StorageFileUploadComponent,
    onSelectFilesClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val state by component.state.subscribeAsState()
    val storageUi = LocalStorageStrings.current.ui
    val filePickerLauncher = rememberFilePickerLauncher { selectedFiles ->
        component.onFilesSelected(selectedFiles)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(CoreTheme.dimens.paddingMedium),
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        // Dropzone area
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .border(
                    width = CoreTheme.dimens.borderWidthThick,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = MaterialTheme.shapes.medium
                )
                .clickable(enabled = enabled && !state.isUploading) {
                    filePickerLauncher.launch()
                    onSelectFilesClick()
                }
                .testTag(StorageFileUploadAreaTestTags.DROPZONE),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CoreTheme.dimens.paddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(CommonRes.drawable.ic_add),
                    contentDescription = null,
                    modifier = Modifier.size(CoreTheme.dimens.iconSizeXLarge),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
                Text(
                    text = storageUi.fileUploadDropzonePrompt,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Queue Items
        if (state.queueItems.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
            ) {
                state.queueItems.forEach { item ->
                    FileUploadQueueItemRow(
                        item = item,
                        isUploading = state.isUploading,
                        localUri = component.getFilePayload(item.id)?.localUri,
                        onRemove = { id -> component.onRemoveItemClicked(id) },
                        onCategoryChanged = { id, category -> component.onItemCategoryChanged(id, category) },
                        onVisibilityChanged = { id, visibility -> component.onItemVisibilityChanged(id, visibility) }
                    )
                }
            }
        }

        // Submit Button
        if (!state.config.autoSubmit && state.hasPending) {
            CoreButton(
                text = storageUi.uploadFileAction,
                onClick = { component.onUploadClicked() },
                enabled = enabled && !state.isUploading,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(StorageFileUploadAreaTestTags.SUBMIT_BUTTON)
            )
        }
    }
}

private data class StorageFileUploadAreaPreviewState(
    val config: StorageFileUploadUiConfig = StorageFileUploadUiConfig(),
    val enabled: Boolean = true
)

@InternalApi
private class StorageFileUploadAreaPreviewProvider : PreviewParameterProvider<StorageFileUploadAreaPreviewState> {
    private val items: List<Pair<String, StorageFileUploadAreaPreviewState>> = listOf(
        "Default" to StorageFileUploadAreaPreviewState(),
        "Disabled" to StorageFileUploadAreaPreviewState(enabled = false)
    )

    override val values: Sequence<StorageFileUploadAreaPreviewState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun StorageFileUploadAreaPreviewContent(state: StorageFileUploadAreaPreviewState) {
    CoreTheme {
        Surface {
            Box(modifier = Modifier.padding(CoreTheme.dimens.paddingMedium)) {
                StorageFileUploadArea(
                    component = StorageFileUploadComponentMock(
                        initialState = StorageFileUploadState(config = state.config)
                    ),
                    onSelectFilesClick = {},
                    enabled = state.enabled
                )
            }
        }
    }
}

@InternalApi
private val defaultStorageFileUploadAreaPreviewState = StorageFileUploadAreaPreviewState()

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StorageFileUploadAreaStatesPreview(
    @PreviewParameter(StorageFileUploadAreaPreviewProvider::class) state: StorageFileUploadAreaPreviewState
) {
    StorageFileUploadAreaPreviewContent(state = state)
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun StorageFileUploadAreaComponentSizePreview() {
    StorageFileUploadAreaPreviewContent(state = defaultStorageFileUploadAreaPreviewState)
}

@InternalApi
@ThemePreviews
@Composable
private fun StorageFileUploadAreaThemePreview() {
    StorageFileUploadAreaPreviewContent(state = defaultStorageFileUploadAreaPreviewState)
}

@InternalApi
@FontScalePreviews
@Composable
private fun StorageFileUploadAreaFontScalePreview() {
    StorageFileUploadAreaPreviewContent(state = defaultStorageFileUploadAreaPreviewState)
}
