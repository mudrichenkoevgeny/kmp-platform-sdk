package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.preview

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.storage.localization.LocalStorageStrings
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.utils.StorageIconsMapper
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.utils.StorageStringsMapper
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory
import org.jetbrains.compose.resources.painterResource

/**
 * Universal component rendering thumbnail container and fallback category icon for a storage file.
 *
 * Renders a styled surface container with a centered vector icon representing the file category into the Composition.
 *
 * @param category Storage category of the file ([StorageCategory]).
 * @param modifier Optional [Modifier] for layout adjustments.
 * @param containerSize Size of the preview container box.
 * @param iconSize Size of the centered category icon.
 */
@Composable
fun StorageFilePreview(
    category: StorageCategory,
    modifier: Modifier = Modifier,
    containerSize: Dp = CoreTheme.dimens.previewThumbnailSize,
    iconSize: Dp = CoreTheme.dimens.iconSizeLarge
) {
    val storageUi = LocalStorageStrings.current.ui
    val categoryDescription = StorageStringsMapper.getCategoryString(category, storageUi)

    Surface(
        modifier = modifier
            .size(containerSize)
            .clip(MaterialTheme.shapes.small)
            .border(
                width = CoreTheme.dimens.borderWidthThin,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.small
            )
            .testTag(StorageFilePreviewTestTags.CONTAINER_PREFIX + category.name),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = MaterialTheme.shapes.small
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(StorageIconsMapper.getCategoryIconRes(category)),
                contentDescription = categoryDescription,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private data class StorageFilePreviewPreviewState(
    val category: StorageCategory
)

@InternalApi
private class StorageFilePreviewPreviewProvider : PreviewParameterProvider<StorageFilePreviewPreviewState> {
    private val items: List<Pair<String, StorageFilePreviewPreviewState>> = listOf(
        "Image" to StorageFilePreviewPreviewState(StorageCategory.IMAGE),
        "Video" to StorageFilePreviewPreviewState(StorageCategory.VIDEO),
        "Audio" to StorageFilePreviewPreviewState(StorageCategory.AUDIO),
        "Document" to StorageFilePreviewPreviewState(StorageCategory.DOCUMENT),
        "Other" to StorageFilePreviewPreviewState(StorageCategory.OTHER)
    )

    override val values: Sequence<StorageFilePreviewPreviewState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun StorageFilePreviewPreviewContent(state: StorageFilePreviewPreviewState) {
    CoreTheme {
        Surface {
            Box(modifier = Modifier.padding(CoreTheme.dimens.paddingMedium)) {
                StorageFilePreview(category = state.category)
            }
        }
    }
}

@InternalApi
private val defaultStorageFilePreviewState = StorageFilePreviewPreviewState(
    category = StorageCategory.IMAGE
)

@InternalApi
@Preview(showBackground = true, group = "Categories")
@Composable
private fun StorageFilePreviewCategoriesPreview(
    @PreviewParameter(StorageFilePreviewPreviewProvider::class) state: StorageFilePreviewPreviewState
) {
    StorageFilePreviewPreviewContent(state = state)
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun StorageFilePreviewComponentSizePreview() {
    StorageFilePreviewPreviewContent(state = defaultStorageFilePreviewState)
}

@InternalApi
@ThemePreviews
@Composable
private fun StorageFilePreviewThemePreview() {
    StorageFilePreviewPreviewContent(state = defaultStorageFilePreviewState)
}

@InternalApi
@FontScalePreviews
@Composable
private fun StorageFilePreviewFontScalePreview() {
    StorageFilePreviewPreviewContent(state = defaultStorageFilePreviewState)
}

/**
 * Test tags dictionary for [StorageFilePreview].
 */
object StorageFilePreviewTestTags {
    /** Test tag prefix for the preview container. */
    const val CONTAINER_PREFIX = "StorageFilePreview_"
}
