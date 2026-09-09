package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error.FullscreenError
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.Res
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditEventDetailScreen(component: AuditEventDetailComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.audit_event_details_title),
                        modifier = Modifier.testTag(AuditEventDetailTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(AuditEventDetailTestTags.BACK_BUTTON)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
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
                is AuditEventDetailScreenState.Loading -> FullscreenLoading()
                is AuditEventDetailScreenState.Error -> FullscreenError(
                    error = currentState.error,
                    onRetry = component::onRetry,
                    modifier = Modifier.testTag(AuditEventDetailTestTags.GLOBAL_ERROR)
                )
                is AuditEventDetailScreenState.Content -> {
                    Content(state = currentState)
                }
            }
        }
    }
}

@Composable
private fun Content(state: AuditEventDetailScreenState.Content) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.paddingMedium)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
    ) {
        Text(
            text = "${stringResource(Res.string.audit_event_id)}: ${state.event.id.value}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(Res.string.audit_event_action) + ": " + state.event.action,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(Res.string.audit_event_resource) + ": " + state.event.resource,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "${stringResource(Res.string.audit_event_status)}: ${state.event.status}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "${stringResource(Res.string.audit_event_actor)}: ${state.event.actorId ?: "N/A"}",
            style = MaterialTheme.typography.bodyMedium
        )
        state.event.message?.let { msg ->
            Text(
                text = "${stringResource(Res.string.audit_event_message)}: $msg",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = "${stringResource(Res.string.audit_event_timestamp)}: ${state.event.createdAt}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

object AuditEventDetailTestTags {
    const val TITLE = "AuditEventDetail_Title"
    const val BACK_BUTTON = "AuditEventDetail_BackButton"
    const val GLOBAL_ERROR = "AuditEventDetail_GlobalError"
}
