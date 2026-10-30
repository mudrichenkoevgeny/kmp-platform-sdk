package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.KmpLocalization
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.storage.localization.LocalStorageStrings
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.utils.StorageIconsMapper
import io.github.mudrichenkoevgeny.shared.foundation.core.common.formatting.ByteSizeFormatter
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.status.StorageFileStatus
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.domain.model.file.createTestStorageFileSummary
import org.jetbrains.compose.resources.painterResource

/**
 * List item component representing a user storage file summary card.
 *
 * @param file Storage file summary domain object to display.
 * @param status Optional file status ([StorageFileStatus]) for displaying lifecycle indicator chip.
 * @param enabled Controls whether interactions are permitted.
 * @param onDeleteClick Optional callback invoked when the delete button is clicked.
 * @param onFileClick Optional callback invoked when the item card is tapped.
 */
@Composable
fun StorageFileItem(
    file: StorageFileSummary,
    status: StorageFileStatus? = null,
    enabled: Boolean = true,
    onDeleteClick: (() -> Unit)? = null,
    onFileClick: (() -> Unit)? = null
) {
    val storageUi = LocalStorageStrings.current.ui

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onFileClick != null) {
                    Modifier.clickable(enabled = enabled) { onFileClick() }
                } else {
                    Modifier
                }
            )
            .testTag(StorageFileItemTestTags.ITEM_PREFIX + file.id.asHexDashString()),
        elevation = CardDefaults.cardElevation(defaultElevation = CoreTheme.dimens.elevationHeader)
    ) {
        Column(
            modifier = Modifier.padding(CoreTheme.dimens.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(StorageIconsMapper.getCategoryIconRes(file.category)),
                    contentDescription = null,
                    modifier = Modifier.size(CoreTheme.dimens.iconSizeLarge),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = file.fileName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                if (status != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = CoreTheme.dimens.paddingSmall,
                                vertical = CoreTheme.dimens.paddingExtraSmall
                            ),
                            horizontalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingExtraSmall),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(StorageIconsMapper.getStatusIconRes(status)),
                                contentDescription = null,
                                modifier = Modifier.size(CoreTheme.dimens.iconSizeSmall),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = status.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(
                        text = file.category.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(
                            horizontal = CoreTheme.dimens.paddingSmall,
                            vertical = CoreTheme.dimens.paddingExtraSmall
                        )
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val formattedSize = remember(file.fileSizeBytes) {
                    ByteSizeFormatter.format(
                        bytesRaw = file.fileSizeBytes,
                        resolver = KmpLocalization.resolver,
                        locale = KmpLocalization.currentLocale
                    ) ?: "${file.fileSizeBytes} B"
                }
                Text(
                    text = formattedSize,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = file.mimeType,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (onDeleteClick != null && status != StorageFileStatus.DELETED) {
                Spacer(Modifier.height(CoreTheme.dimens.paddingExtraSmall))
                HorizontalDivider()
                Spacer(Modifier.height(CoreTheme.dimens.paddingExtraSmall))
                Button(
                    onClick = onDeleteClick,
                    modifier = Modifier.align(Alignment.End),
                    enabled = enabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text(storageUi.deleteFile)
                }
            }
        }
    }
}

private data class StorageFileItemPreviewState(
    val file: StorageFileSummary,
    val status: StorageFileStatus? = StorageFileStatus.READY,
    val enabled: Boolean = true
)

@InternalApi
private class StorageFileItemPreviewProvider : PreviewParameterProvider<StorageFileItemPreviewState> {
    private val items: List<Pair<String, StorageFileItemPreviewState>> = listOf(
        "Default" to StorageFileItemPreviewState(
            file = createTestStorageFileSummary()
        ),
        "Pending Upload" to StorageFileItemPreviewState(
            file = createTestStorageFileSummary(),
            status = StorageFileStatus.PENDING_UPLOAD
        ),
        "Processing" to StorageFileItemPreviewState(
            file = createTestStorageFileSummary(),
            status = StorageFileStatus.PROCESSING
        ),
        "Deleted" to StorageFileItemPreviewState(
            file = createTestStorageFileSummary(),
            status = StorageFileStatus.DELETED
        ),
        "Disabled" to StorageFileItemPreviewState(
            file = createTestStorageFileSummary(),
            enabled = false
        )
    )

    override val values: Sequence<StorageFileItemPreviewState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun StorageFileItemPreviewContent(state: StorageFileItemPreviewState) {
    CoreTheme {
        Surface {
            Box(modifier = Modifier.padding(CoreTheme.dimens.paddingLarge)) {
                StorageFileItem(
                    file = state.file,
                    status = state.status,
                    enabled = state.enabled,
                    onDeleteClick = {}
                )
            }
        }
    }
}

@InternalApi
private val defaultStorageFileItemPreviewState = StorageFileItemPreviewState(
    file = createTestStorageFileSummary()
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StorageFileItemStatesPreview(
    @PreviewParameter(StorageFileItemPreviewProvider::class) state: StorageFileItemPreviewState
) {
    StorageFileItemPreviewContent(state = state)
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun StorageFileItemComponentSizePreview() {
    StorageFileItemPreviewContent(state = defaultStorageFileItemPreviewState)
}

@InternalApi
@ThemePreviews
@Composable
private fun StorageFileItemThemePreview() {
    StorageFileItemPreviewContent(state = defaultStorageFileItemPreviewState)
}

@InternalApi
@FontScalePreviews
@Composable
private fun StorageFileItemFontScalePreview() {
    StorageFileItemPreviewContent(state = defaultStorageFileItemPreviewState)
}

object StorageFileItemTestTags {
    const val ITEM_PREFIX = "StorageFileItem_"
}
