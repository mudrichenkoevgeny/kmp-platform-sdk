package io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.preview

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.category.StorageCategory

/**
 * Image and thumbnail preview component using Coil for asynchronous loading, caching, and category fallback rendering.
 *
 * Renders an asynchronous image thumbnail into the Composition, falling back to a category icon container on failure.
 *
 * @param imageUrl Target URL of the image or video thumbnail to load.
 * @param category Storage category ([StorageCategory]) used for fallback rendering on loading failure.
 * @param modifier Optional [Modifier] for layout adjustments.
 * @param containerHeight Fixed height of the preview container.
 */
@Composable
fun StorageFileImagePreview(
    imageUrl: String,
    category: StorageCategory,
    modifier: Modifier = Modifier,
    containerHeight: Dp = CoreTheme.dimens.previewContainerHeight
) {
    Surface(
        modifier = modifier
            .height(containerHeight)
            .clip(MaterialTheme.shapes.small)
            .border(
                width = CoreTheme.dimens.borderWidthThin,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.small
            )
            .testTag(StorageFileImagePreviewTestTags.CONTAINER),
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        ) {
            val painterState by painter.state.collectAsState()
            when (painterState) {
                is AsyncImagePainter.State.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        FullscreenLoading()
                    }
                }
                is AsyncImagePainter.State.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        StorageFilePreview(
                            category = category,
                            containerSize = containerHeight,
                            iconSize = CoreTheme.dimens.iconSizeXLarge
                        )
                    }
                }
                is AsyncImagePainter.State.Success,
                is AsyncImagePainter.State.Empty -> {
                    SubcomposeAsyncImageContent(
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

private data class StorageFileImagePreviewPreviewState(
    val imageUrl: String,
    val category: StorageCategory
)

@InternalApi
private class StorageFileImagePreviewPreviewProvider : PreviewParameterProvider<StorageFileImagePreviewPreviewState> {
    private val items: List<Pair<String, StorageFileImagePreviewPreviewState>> = listOf(
        "Image" to StorageFileImagePreviewPreviewState(
            imageUrl = "https://example.com/image.png",
            category = StorageCategory.IMAGE
        ),
        "Video" to StorageFileImagePreviewPreviewState(
            imageUrl = "https://example.com/video_thumb.png",
            category = StorageCategory.VIDEO
        )
    )

    override val values: Sequence<StorageFileImagePreviewPreviewState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun StorageFileImagePreviewPreviewContent(state: StorageFileImagePreviewPreviewState) {
    CoreTheme {
        Surface {
            Box(modifier = Modifier.padding(CoreTheme.dimens.paddingMedium)) {
                StorageFileImagePreview(
                    imageUrl = state.imageUrl,
                    category = state.category
                )
            }
        }
    }
}

@InternalApi
private val defaultStorageFileImagePreviewPreviewState = StorageFileImagePreviewPreviewState(
    imageUrl = "https://example.com/image.png",
    category = StorageCategory.IMAGE
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StorageFileImagePreviewStatesPreview(
    @PreviewParameter(StorageFileImagePreviewPreviewProvider::class) state: StorageFileImagePreviewPreviewState
) {
    StorageFileImagePreviewPreviewContent(state = state)
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun StorageFileImagePreviewComponentSizePreview() {
    StorageFileImagePreviewPreviewContent(state = defaultStorageFileImagePreviewPreviewState)
}

@InternalApi
@ThemePreviews
@Composable
private fun StorageFileImagePreviewThemePreview() {
    StorageFileImagePreviewPreviewContent(state = defaultStorageFileImagePreviewPreviewState)
}

@InternalApi
@FontScalePreviews
@Composable
private fun StorageFileImagePreviewFontScalePreview() {
    StorageFileImagePreviewPreviewContent(state = defaultStorageFileImagePreviewPreviewState)
}

/**
 * Test tags dictionary for [StorageFileImagePreview].
 */
object StorageFileImagePreviewTestTags {
    /** Test tag for the image preview container. */
    const val CONTAINER = "StorageFileImagePreview_Container"
}
