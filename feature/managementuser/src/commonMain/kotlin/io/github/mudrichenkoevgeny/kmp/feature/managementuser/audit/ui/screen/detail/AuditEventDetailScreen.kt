package io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error.FullscreenError
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreSmallText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.*
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.domain.model.event.auditEventMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.ui.screen.detail.AuditEventDetailComponentMock
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditEventDetailScreen(component: AuditEventDetailComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.audit_event_details_title),
                        modifier = Modifier.testTag(AuditEventDetailTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(AuditEventDetailTestTags.BACK_BUTTON)
                    )
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
            .padding(CoreTheme.dimens.paddingMedium)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CoreTitleText(
            text = "${stringResource(Res.string.audit_event_id)}: ${state.event.id.value}",
            style = MaterialTheme.typography.titleMedium
        )
        CoreBodyText(
            text = stringResource(Res.string.audit_event_action) + ": " + state.event.action,
            style = MaterialTheme.typography.bodyMedium
        )
        CoreBodyText(
            text = stringResource(Res.string.audit_event_resource) + ": " + state.event.resource,
            style = MaterialTheme.typography.bodyMedium
        )
        CoreBodyText(
            text = "${stringResource(Res.string.audit_event_status)}: ${state.event.status}",
            style = MaterialTheme.typography.bodyMedium
        )
        CoreBodyText(
            text = "${stringResource(Res.string.audit_event_actor)}: ${state.event.actorId ?: "N/A"}",
            style = MaterialTheme.typography.bodyMedium
        )
        state.event.message?.let { msg ->
            CoreBodyText(
                text = "${stringResource(Res.string.audit_event_message)}: $msg",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        CoreSmallText(
            text = "${stringResource(Res.string.audit_event_timestamp)}: ${state.event.createdAt}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@InternalApi
internal class AuditEventDetailPreviewProvider : PreviewParameterProvider<AuditEventDetailScreenState> {
    @InternalApi
    private val items: List<Pair<String, AuditEventDetailScreenState>> = listOf(
        "Content" to AuditEventDetailScreenState.Content(event = auditEventMock()),
        "Error" to AuditEventDetailScreenState.Error(error = CommonError.Unknown()),
        "Loading" to AuditEventDetailScreenState.Loading
    )

    override val values: Sequence<AuditEventDetailScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun AuditEventDetailScreenPreviewContent(state: AuditEventDetailScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        AuditEventDetailScreen(
            component = AuditEventDetailComponentMock(initialState = state)
        )
    }
}

@InternalApi
private val defaultAuditEventDetailPreviewState = AuditEventDetailScreenState.Content(
    event = auditEventMock()
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(AuditEventDetailPreviewProvider::class) state: AuditEventDetailScreenState
) {
    ScreenPreviewContainer {
        AuditEventDetailScreenPreviewContent(state = state)
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        AuditEventDetailScreenPreviewContent(state = defaultAuditEventDetailPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        AuditEventDetailScreenPreviewContent(state = defaultAuditEventDetailPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        AuditEventDetailScreenPreviewContent(state = defaultAuditEventDetailPreviewState)
    }
}

object AuditEventDetailTestTags {
    const val TITLE = "AuditEventDetail_Title"
    const val BACK_BUTTON = "AuditEventDetail_BackButton"
    const val GLOBAL_ERROR = "AuditEventDetail_GlobalError"
}
