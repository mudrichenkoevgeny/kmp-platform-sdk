package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.mudrichenkoevgeny.kmp.core.common.ic_delete
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.KmpLocalization
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewComponentSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.storage.ic_file_status_ready
import io.github.mudrichenkoevgeny.kmp.core.storage.localization.LocalStorageStrings
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.preview.StorageFileImagePreview
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.preview.StorageFilePreview
import io.github.mudrichenkoevgeny.shared.foundation.core.common.formatting.ByteSizeFormatter
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.UploadQueueItem
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.UploadQueueItemId
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.UploadQueueItemStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.upload.toUploadQueueItemIdOrThrow
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.visibility.StorageFileVisibility
import org.jetbrains.compose.resources.painterResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes
import io.github.mudrichenkoevgeny.kmp.core.storage.Res as StorageRes

/**
 * Individual row component representing a queued file item in the upload area.
 *
 * @param item Queue item domain model from shared foundation.
 * @param isUploading Whether the upload area is actively uploading.
 * @param onRemove Callback invoked when the user taps to remove the item from the queue.
 * @param modifier Optional [Modifier] for layout adjustments.
 */
@Composable
public fun FileUploadQueueItemRow(
    item: UploadQueueItem,
    isUploading: Boolean,
    onRemove: (UploadQueueItemId) -> Unit,
    modifier: Modifier = Modifier,
    localUri: String? = null,
    onCategoryChanged: ((UploadQueueItemId, StorageCategory) -> Unit)? = null,
    onVisibilityChanged: ((UploadQueueItemId, StorageFileVisibility) -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(StorageFileUploadAreaTestTags.QUEUE_ITEM),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(CoreTheme.dimens.paddingSmall),
            verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingExtraSmall)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QueueItemInfoSection(
                    item = item,
                    localUri = localUri,
                    modifier = Modifier.weight(1f)
                )
                QueueItemActionsSection(
                    item = item,
                    isUploading = isUploading,
                    onRemove = onRemove,
                    onCategoryChanged = onCategoryChanged,
                    onVisibilityChanged = onVisibilityChanged
                )
            }

            if (item.status == UploadQueueItemStatus.UPLOADING) {
                QueueItemProgressSection(progressPercent = item.progressPercent)
            }

            if (item.status == UploadQueueItemStatus.FAILED && item.error != null) {
                CoreErrorText(
                    text = item.error?.message ?: "",
                    modifier = Modifier.testTag(StorageFileUploadAreaTestTags.ERROR_TEXT)
                )
            }
        }
    }
}

@Composable
private fun QueueItemInfoSection(
    item: UploadQueueItem,
    localUri: String?,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall),
        modifier = modifier
    ) {
        if (!localUri.isNullOrBlank() && item.category == StorageCategory.IMAGE) {
            StorageFileImagePreview(
                imageUrl = localUri,
                category = item.category,
                modifier = Modifier.size(CoreTheme.dimens.previewThumbnailSize),
                containerHeight = CoreTheme.dimens.previewThumbnailSize
            )
        } else {
            StorageFilePreview(
                category = item.category,
                containerSize = CoreTheme.dimens.previewThumbnailSize,
                iconSize = CoreTheme.dimens.iconSizeMedium
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.fileName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            val formattedSize = remember(item.fileSizeBytes) {
                ByteSizeFormatter.format(
                    bytesRaw = item.fileSizeBytes,
                    resolver = KmpLocalization.resolver,
                    locale = KmpLocalization.currentLocale
                ) ?: "${item.fileSizeBytes} B"
            }
            Text(
                text = formattedSize,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun QueueItemCategoryDropdown(
    item: UploadQueueItem,
    isUploading: Boolean,
    onCategoryChanged: (UploadQueueItemId, StorageCategory) -> Unit
) {
    var isCategoryMenuExpanded by remember { mutableStateOf(false) }
    Box {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = MaterialTheme.shapes.extraSmall,
            modifier = Modifier.clickable(!isUploading) { isCategoryMenuExpanded = true }
        ) {
            Text(
                text = item.category.value,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(
                    horizontal = CoreTheme.dimens.paddingSmall,
                    vertical = CoreTheme.dimens.paddingExtraSmall
                )
            )
        }
        DropdownMenu(
            expanded = isCategoryMenuExpanded,
            onDismissRequest = { isCategoryMenuExpanded = false }
        ) {
            StorageCategory.entries.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.value) },
                    onClick = {
                        isCategoryMenuExpanded = false
                        onCategoryChanged(item.id, category)
                    }
                )
            }
        }
    }
}

@Composable
private fun QueueItemVisibilityDropdown(
    item: UploadQueueItem,
    isUploading: Boolean,
    onVisibilityChanged: (UploadQueueItemId, StorageFileVisibility) -> Unit
) {
    var isVisibilityMenuExpanded by remember { mutableStateOf(false) }
    Box {
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = MaterialTheme.shapes.extraSmall,
            modifier = Modifier.clickable(!isUploading) { isVisibilityMenuExpanded = true }
        ) {
            Text(
                text = item.visibility.value,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(
                    horizontal = CoreTheme.dimens.paddingSmall,
                    vertical = CoreTheme.dimens.paddingExtraSmall
                )
            )
        }
        DropdownMenu(
            expanded = isVisibilityMenuExpanded,
            onDismissRequest = { isVisibilityMenuExpanded = false }
        ) {
            StorageFileVisibility.entries.forEach { visibility ->
                DropdownMenuItem(
                    text = { Text(visibility.value) },
                    onClick = {
                        isVisibilityMenuExpanded = false
                        onVisibilityChanged(item.id, visibility)
                    }
                )
            }
        }
    }
}

@Composable
private fun QueueItemActionsSection(
    item: UploadQueueItem,
    isUploading: Boolean,
    onRemove: (UploadQueueItemId) -> Unit,
    onCategoryChanged: ((UploadQueueItemId, StorageCategory) -> Unit)?,
    onVisibilityChanged: ((UploadQueueItemId, StorageFileVisibility) -> Unit)?
) {
    val storageUi = LocalStorageStrings.current.ui

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingExtraSmall)
    ) {
        if (onCategoryChanged != null && item.status != UploadQueueItemStatus.UPLOADING) {
            QueueItemCategoryDropdown(
                item = item,
                isUploading = isUploading,
                onCategoryChanged = onCategoryChanged
            )
        }

        if (onVisibilityChanged != null && item.status != UploadQueueItemStatus.UPLOADING) {
            QueueItemVisibilityDropdown(
                item = item,
                isUploading = isUploading,
                onVisibilityChanged = onVisibilityChanged
            )
        }

        if (item.status == UploadQueueItemStatus.COMPLETED) {
            Icon(
                painter = painterResource(StorageRes.drawable.ic_file_status_ready),
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(CoreTheme.dimens.iconSizeMedium)
            )
        }

        if (item.status != UploadQueueItemStatus.UPLOADING &&
            item.status != UploadQueueItemStatus.COMPLETED
        ) {
            IconButton(
                onClick = { onRemove(item.id) },
                enabled = !isUploading,
                modifier = Modifier.testTag(StorageFileUploadAreaTestTags.REMOVE_ITEM_BUTTON)
            ) {
                Icon(
                    painter = painterResource(CommonRes.drawable.ic_delete),
                    contentDescription = storageUi.deleteFile,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun QueueItemProgressSection(
    progressPercent: Int
) {
    val storageUi = LocalStorageStrings.current.ui

    Column(
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingExtraSmall)
    ) {
        LinearProgressIndicator(
            progress = { progressPercent / 100f },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = storageUi.fileUploadingProgress(progressPercent),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

private data class FileUploadQueueItemRowPreviewState(
    val item: UploadQueueItem,
    val isUploading: Boolean = false
)

@InternalApi
private class FileUploadQueueItemRowPreviewProvider : PreviewParameterProvider<FileUploadQueueItemRowPreviewState> {
    private val sampleItemId = "00000000-0000-0000-0000-000000000001".toUploadQueueItemIdOrThrow()
    private val items: List<Pair<String, FileUploadQueueItemRowPreviewState>> = listOf(
        "Idle" to FileUploadQueueItemRowPreviewState(
            item = UploadQueueItem(
                id = sampleItemId,
                fileName = "document.pdf",
                fileSizeBytes = 2048576L,
                mimeType = "application/pdf",
                category = StorageCategory.DOCUMENT,
                status = UploadQueueItemStatus.IDLE
            )
        ),
        "Uploading" to FileUploadQueueItemRowPreviewState(
            item = UploadQueueItem(
                id = sampleItemId,
                fileName = "photo.jpg",
                fileSizeBytes = 512000L,
                mimeType = "image/jpeg",
                category = StorageCategory.IMAGE,
                status = UploadQueueItemStatus.UPLOADING,
                progressPercent = 45
            ),
            isUploading = true
        ),
        "Completed" to FileUploadQueueItemRowPreviewState(
            item = UploadQueueItem(
                id = sampleItemId,
                fileName = "archive.zip",
                fileSizeBytes = 10485760L,
                mimeType = "application/zip",
                category = StorageCategory.OTHER,
                status = UploadQueueItemStatus.COMPLETED
            )
        )
    )

    override val values: Sequence<FileUploadQueueItemRowPreviewState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun FileUploadQueueItemRowPreviewContent(state: FileUploadQueueItemRowPreviewState) {
    CoreTheme {
        Surface {
            Box(modifier = Modifier.padding(CoreTheme.dimens.paddingMedium)) {
                FileUploadQueueItemRow(
                    item = state.item,
                    isUploading = state.isUploading,
                    onRemove = {}
                )
            }
        }
    }
}

@InternalApi
private val defaultFileUploadQueueItemRowPreviewState = FileUploadQueueItemRowPreviewState(
    item = UploadQueueItem(
        id = "00000000-0000-0000-0000-000000000001".toUploadQueueItemIdOrThrow(),
        fileName = "document.pdf",
        fileSizeBytes = 2048576L,
        mimeType = "application/pdf",
        category = StorageCategory.DOCUMENT,
        status = UploadQueueItemStatus.IDLE
    )
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun FileUploadQueueItemRowStatesPreview(
    @PreviewParameter(FileUploadQueueItemRowPreviewProvider::class) state: FileUploadQueueItemRowPreviewState
) {
    FileUploadQueueItemRowPreviewContent(state = state)
}

@InternalApi
@PreviewComponentSizes
@Composable
private fun FileUploadQueueItemRowComponentSizePreview() {
    FileUploadQueueItemRowPreviewContent(state = defaultFileUploadQueueItemRowPreviewState)
}

@InternalApi
@PreviewThemes
@Composable
private fun FileUploadQueueItemRowThemePreview() {
    FileUploadQueueItemRowPreviewContent(state = defaultFileUploadQueueItemRowPreviewState)
}

@InternalApi
@PreviewFontScales
@Composable
private fun FileUploadQueueItemRowFontScalePreview() {
    FileUploadQueueItemRowPreviewContent(state = defaultFileUploadQueueItemRowPreviewState)
}
