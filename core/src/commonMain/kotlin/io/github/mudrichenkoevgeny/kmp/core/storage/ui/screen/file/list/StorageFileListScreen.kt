package io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreTextButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.ListingEmptyState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.ListingHeaderBar
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.OnBottomReach
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.PagingFooter
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.option.ListingOptionsPanel
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.scrollbar.CoreLazyColumnScrollbar
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewScreenSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.generated.resources.ic_add
import io.github.mudrichenkoevgeny.kmp.core.generated.resources.ic_filter
import io.github.mudrichenkoevgeny.kmp.core.generated.resources.ic_refresh
import io.github.mudrichenkoevgeny.kmp.core.storage.localization.LocalStorageStrings
import io.github.mudrichenkoevgeny.kmp.core.storage.mock.ui.screen.file.list.StorageFileListComponentMock
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.component.file.item.StorageFileItem
import io.github.mudrichenkoevgeny.kmp.core.storage.ui.screen.file.list.dialog.StorageFileListDialogChild
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.file.StorageFileSummary
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.domain.model.status.StorageFileStatus
import org.jetbrains.compose.resources.painterResource
import io.github.mudrichenkoevgeny.kmp.core.generated.resources.Res as CommonRes

/**
 * Universal Screen displaying a paginated list of storage files (usable in both client and management contexts).
 *
 * @param component Decompose controller driving state and callbacks.
 * @param modifier Optional [Modifier] for screen positioning.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun StorageFileListScreen(
    component: StorageFileListComponent,
    modifier: Modifier = Modifier
) {
    val currentState by component.state.subscribeAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            StorageFileListTopBar(component = component)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (currentState) {
                is StorageFileListScreenState.Loading -> FullscreenLoading()
                is StorageFileListScreenState.Content -> {
                    val contentState = currentState as StorageFileListScreenState.Content
                    if (contentState.paging.isInitialLoading && contentState.paging.items.isEmpty()) {
                        FullscreenLoading()
                    } else {
                        Content(
                            state = contentState,
                            component = component
                        )
                    }
                }

                is StorageFileListScreenState.Error -> {
                    CoreErrorText(
                        text = (currentState as StorageFileListScreenState.Error).error.toLocalizedMessage(),
                        modifier = Modifier.testTag(StorageFileListTestTags.GLOBAL_ERROR_TEXT)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StorageFileListTopBar(
    component: StorageFileListComponent
) {
    val storageUi = LocalStorageStrings.current.ui
    TopAppBar(
        title = {
            CoreScreenTitleText(
                text = storageUi.storageFileListTitle,
                modifier = Modifier.testTag(StorageFileListTestTags.TITLE)
            )
        },
        navigationIcon = {
            CoreBackButton(
                onClick = component::onBackClick,
                modifier = Modifier.testTag(StorageFileListTestTags.BACK_BUTTON)
            )
        },
        actions = {
            if (component.isUploadAvailable) {
                IconButton(
                    onClick = component::onUploadClick,
                    modifier = Modifier.testTag(StorageFileListTestTags.UPLOAD_BUTTON)
                ) {
                    Icon(
                        painter = painterResource(CommonRes.drawable.ic_add),
                        contentDescription = null,
                        modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                    )
                }
            }
            IconButton(
                onClick = component::onToggleFilterPanel,
                modifier = Modifier.testTag(StorageFileListTestTags.FILTER_BUTTON)
            ) {
                Icon(
                    painter = painterResource(CommonRes.drawable.ic_filter),
                    contentDescription = null,
                    modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                )
            }
            IconButton(
                onClick = component::onRefresh,
                modifier = Modifier.testTag(StorageFileListTestTags.REFRESH_BUTTON)
            ) {
                Icon(
                    painter = painterResource(CommonRes.drawable.ic_refresh),
                    contentDescription = null,
                    modifier = Modifier.padding(CoreTheme.dimens.paddingExtraSmall)
                )
            }
        }
    )
}

@Composable
private fun Content(
    state: StorageFileListScreenState.Content,
    component: StorageFileListComponent,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    listState.OnBottomReach {
        component.onLoadNextPage()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        StorageFileListBody(
            state = state,
            component = component,
            listState = listState,
            modifier = Modifier
                .widthIn(max = CoreTheme.dimens.maxContentWidth)
                .fillMaxSize()
        )

        CoreLazyColumnScrollbar(
            lazyListState = listState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        )
    }

    StorageFileListDialogs(
        component = component,
        selectedCount = state.selectedFileIds.size
    )
}

@Composable
private fun StorageFileListBody(
    state: StorageFileListScreenState.Content,
    component: StorageFileListComponent,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        StorageFileListFilterPanel(
            state = state,
            component = component
        )

        state.actionError?.let { actionError ->
            CoreErrorText(
                text = actionError.toLocalizedMessage(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CoreTheme.dimens.paddingMedium)
                    .testTag(StorageFileListTestTags.ACTION_ERROR_TEXT)
            )
        }

        if (state.selectedFileIds.isNotEmpty()) {
            StorageFileListBatchActionBar(
                selectedCount = state.selectedFileIds.size,
                component = component
            )
        }

        if (state.paging.isEmpty && (!state.paging.isInitialLoading) && (state.paging.error == null)) {
            ListingEmptyState(
                modifier = Modifier.weight(1f)
            )
        } else {
            ListingHeaderBar(
                state = state.paging,
                lazyListState = listState
            )

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .testTag(StorageFileListTestTags.FILE_LIST),
                contentPadding = PaddingValues(CoreTheme.dimens.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
            ) {
                items(
                    items = state.paging.items,
                    key = { it.id.asHexDashString() }
                ) { file ->
                    StorageFileListItemRow(
                        file = file,
                        isSelected = state.selectedFileIds.contains(file.id.asHexDashString()),
                        actionLoading = state.actionLoading,
                        component = component
                    )
                }

                item {
                    PagingFooter(
                        state = state.paging,
                        onRetry = component::onLoadNextPage
                    )
                }
            }
        }
    }
}

@Composable
private fun StorageFileListFilterPanel(
    state: StorageFileListScreenState.Content,
    component: StorageFileListComponent
) {
    AnimatedVisibility(visible = state.isFilterPanelExpanded) {
        ListingOptionsPanel(
            config = getStorageFileListOptionsConfig(includeOwnerFilter = component.includeOwnerFilter),
            sortState = state.sortState,
            filterStates = state.filterStates,
            onSortChange = { sortState -> component.onSortChanged(sortState) },
            onFilterChange = { key, filterState -> component.onFilterChanged(key, filterState) },
            onApplyClick = component::onApplyFilters,
            modifier = Modifier
                .fillMaxWidth()
                .padding(CoreTheme.dimens.paddingMedium)
        )
    }
}

@Composable
private fun StorageFileListBatchActionBar(
    selectedCount: Int,
    component: StorageFileListComponent
) {
    val commonUi = LocalCommonStrings.current.ui
    val storageUi = LocalStorageStrings.current.ui

    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(StorageFileListTestTags.BATCH_ACTION_BAR)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = CoreTheme.dimens.paddingMedium,
                    vertical = CoreTheme.dimens.paddingSmall
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = commonUi.selectedCount(selectedCount),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingExtraSmall)
            ) {
                TextButton(
                    onClick = component::selectAllFiles,
                    modifier = Modifier.testTag(StorageFileListTestTags.SELECT_ALL_BUTTON)
                ) {
                    Text(commonUi.selectAll)
                }
                TextButton(
                    onClick = component::clearFileSelection,
                    modifier = Modifier.testTag(StorageFileListTestTags.CLEAR_SELECTION_BUTTON)
                ) {
                    Text(commonUi.clearAll)
                }
                if (component.isBatchDeleteAvailable) {
                    TextButton(
                        onClick = component::onDeleteSelectedFilesClick,
                        modifier = Modifier.testTag(StorageFileListTestTags.BATCH_DELETE_BUTTON)
                    ) {
                        Text(
                            text = storageUi.deleteFileAction,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StorageFileListItemRow(
    file: StorageFileSummary,
    isSelected: Boolean,
    actionLoading: Boolean,
    component: StorageFileListComponent
) {
    val fileIdString = file.id.asHexDashString()
    val isDeleted = file.status == StorageFileStatus.DELETED

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
    ) {
        if (component.isBatchDeleteAvailable) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { component.toggleSelectFile(fileIdString) },
                enabled = !isDeleted && !actionLoading
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            StorageFileItem(
                file = file,
                enabled = !actionLoading,
                onDeleteClick = if (component.isSingleDeleteAvailable && !isDeleted) {
                    { component.onDeleteFileClick(fileIdString) }
                } else {
                    null
                },
                onFileClick = { component.onFileClick(file) }
            )
        }
    }
}

@Composable
private fun StorageFileListDialogs(
    component: StorageFileListComponent,
    selectedCount: Int
) {
    val storageUi = LocalStorageStrings.current.ui
    val commonUi = LocalCommonStrings.current.ui
    val dialogSlot by component.dialogSlot.subscribeAsState()
    val dialogChild = dialogSlot.child?.instance

    if (dialogChild != null) {
        when (dialogChild) {
            is StorageFileListDialogChild.ConfirmDeleteFile -> {
                AlertDialog(
                    onDismissRequest = component::onDismissDeleteDialog,
                    title = { CoreTitleText(text = storageUi.deleteFile) },
                    text = { CoreBodyText(text = storageUi.deleteFileConfirm) },
                    confirmButton = {
                        CoreTextButton(
                            text = storageUi.deleteFileAction,
                            onClick = component::onConfirmDeleteFile
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

            is StorageFileListDialogChild.ConfirmDeleteBatchFiles -> {
                AlertDialog(
                    onDismissRequest = component::onDismissDeleteDialog,
                    title = { CoreTitleText(text = storageUi.deleteSelected) },
                    text = { CoreBodyText(text = storageUi.deleteFilesBatchConfirm(selectedCount)) },
                    confirmButton = {
                        CoreTextButton(
                            text = storageUi.deleteSelected,
                            onClick = component::onConfirmBatchDeleteFiles
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

@InternalApi
internal class StorageFileListPreviewProvider : PreviewParameterProvider<StorageFileListScreenState> {
    private val items = listOf(
        "Content" to StorageFileListScreenState.Content(),
        "Error" to StorageFileListScreenState.Error(error = CommonError.Unknown()),
        "Loading" to StorageFileListScreenState.Loading
    )

    override val values: Sequence<StorageFileListScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun StorageFileListScreenPreviewContent(state: StorageFileListScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        StorageFileListScreen(
            component = StorageFileListComponentMock(initialState = state)
        )
    }
}

@InternalApi
private val defaultStorageFileListPreviewState = StorageFileListScreenState.Content()

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(StorageFileListPreviewProvider::class) state: StorageFileListScreenState
) {
    ScreenPreviewContainer {
        StorageFileListScreenPreviewContent(state = state)
    }
}

@InternalApi
@PreviewScreenSizes
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        StorageFileListScreenPreviewContent(state = defaultStorageFileListPreviewState)
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        StorageFileListScreenPreviewContent(state = defaultStorageFileListPreviewState)
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        StorageFileListScreenPreviewContent(state = defaultStorageFileListPreviewState)
    }
}
