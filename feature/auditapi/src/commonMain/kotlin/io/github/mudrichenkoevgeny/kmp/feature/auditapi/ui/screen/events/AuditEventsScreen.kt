package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.OnBottomReached
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.listing.PagingFooter
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.Res
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.*
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.component.audit.item.AuditItem
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditEventsScreen(component: AuditEventsComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.audit_logs_title),
                        modifier = Modifier.testTag(AuditEventsTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(AuditEventsTestTags.BACK_BUTTON)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = component::onRefresh,
                        modifier = Modifier.testTag(AuditEventsTestTags.REFRESH_BUTTON)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                    }
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
                is AuditEventsScreenState.Loading -> FullscreenLoading()
                is AuditEventsScreenState.Content -> {
                    if (currentState.paging.isInitialLoading) {
                        FullscreenLoading()
                    } else {
                        Content(
                            state = currentState,
                            onEventClick = component::onEventClick,
                            onLoadNextPage = component::onLoadNextPage
                        )
                    }
                }
                is AuditEventsScreenState.Error -> {
                    Text(
                        text = currentState.error.toLocalizedMessage(),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.testTag(AuditEventsTestTags.GLOBAL_ERROR_TEXT)
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: AuditEventsScreenState.Content,
    onEventClick: (AuditEventId) -> Unit,
    onLoadNextPage: () -> Unit
) {
    val listState = rememberLazyListState()

    listState.OnBottomReached {
        onLoadNextPage()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .testTag(AuditEventsTestTags.EVENT_LIST),
            contentPadding = PaddingValues(Dimens.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
        ) {
            items(state.paging.items, key = { it.id.value }) { event ->
                AuditItem(
                    event = event,
                    onClick = { onEventClick(event.id) }
                )
            }

            item {
                PagingFooter(
                    state = state.paging,
                    onRetry = onLoadNextPage
                )
            }
        }
    }
}

object AuditEventsTestTags {
    const val TITLE = "AuditEvents_Title"
    const val BACK_BUTTON = "AuditEvents_BackButton"
    const val REFRESH_BUTTON = "AuditEvents_RefreshButton"
    const val GLOBAL_ERROR_TEXT = "AuditEvents_GlobalErrorText"
    const val EVENT_LIST = "AuditEvents_List"
    const val AUDIT_ITEM_PREFIX = "AuditEvents_Item_"
}
