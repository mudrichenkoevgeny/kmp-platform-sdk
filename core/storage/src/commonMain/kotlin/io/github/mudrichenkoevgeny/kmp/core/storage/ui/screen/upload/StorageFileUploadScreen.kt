package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.upload

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.container.CoreScrollableScreenContent
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.storage.localization.LocalStorageStrings
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.component.file.upload.StorageFileUploadComponentMock
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.upload.StorageFileUploadScreenComponentMock
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.StorageFileUploadArea
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.upload.StorageFileUploadState
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.ui.component.fileupload.StorageFileUploadUiConfig

/**
 * Universal Screen providing UI wrapper for selecting and uploading storage files.
 *
 * @param component Presentation logic controller for file upload screen operations.
 * @param modifier Optional [Modifier] for screen positioning.
 * @param enabled Whether UI controls and selection are active.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageFileUploadScreen(
    component: StorageFileUploadScreenComponent,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val storageUi = LocalStorageStrings.current.ui

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = storageUi.fileUploadTitle,
                        modifier = Modifier.testTag(StorageFileUploadTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(StorageFileUploadTestTags.BACK_BUTTON)
                    )
                }
            )
        }
    ) { padding ->
        CoreScrollableScreenContent(
            modifier = Modifier.padding(padding)
        ) {
            StorageFileUploadArea(
                component = component.uploadComponent,
                onSelectFilesClick = component::onSelectFilesClick,
                enabled = enabled
            )
        }
    }
}

private data class StorageFileUploadScreenPreviewState(
    val config: StorageFileUploadUiConfig = StorageFileUploadUiConfig(),
    val enabled: Boolean = true
)

@InternalApi
private class StorageFileUploadScreenPreviewProvider : PreviewParameterProvider<StorageFileUploadScreenPreviewState> {
    private val items: List<Pair<String, StorageFileUploadScreenPreviewState>> = listOf(
        "Default" to StorageFileUploadScreenPreviewState(),
        "Disabled" to StorageFileUploadScreenPreviewState(enabled = false)
    )

    override val values: Sequence<StorageFileUploadScreenPreviewState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun StorageFileUploadScreenPreviewContent(state: StorageFileUploadScreenPreviewState) {
    ScreenPreviewContainer {
        CoreTheme {
            Surface {
                Box {
                    StorageFileUploadScreen(
                        component = StorageFileUploadScreenComponentMock(
                            uploadComponent = StorageFileUploadComponentMock(
                                initialState = StorageFileUploadState(config = state.config)
                            )
                        ),
                        enabled = state.enabled
                    )
                }
            }
        }
    }
}

@InternalApi
private val defaultStorageFileUploadScreenPreviewState = StorageFileUploadScreenPreviewState()

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StorageFileUploadScreenStatesPreview(
    @PreviewParameter(StorageFileUploadScreenPreviewProvider::class) state: StorageFileUploadScreenPreviewState
) {
    StorageFileUploadScreenPreviewContent(state = state)
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun StorageFileUploadScreenComponentSizePreview() {
    StorageFileUploadScreenPreviewContent(state = defaultStorageFileUploadScreenPreviewState)
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun StorageFileUploadScreenSizePreview() {
    StorageFileUploadScreenPreviewContent(state = defaultStorageFileUploadScreenPreviewState)
}

@InternalApi
@ThemePreviews
@Composable
private fun StorageFileUploadScreenThemePreview() {
    StorageFileUploadScreenPreviewContent(state = defaultStorageFileUploadScreenPreviewState)
}

@InternalApi
@FontScalePreviews
@Composable
private fun StorageFileUploadScreenFontScalePreview() {
    StorageFileUploadScreenPreviewContent(state = defaultStorageFileUploadScreenPreviewState)
}
