package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import io.github.mudrichenkoevgeny.kmp.core.common.time.formatInstantToDateTime
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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_action
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_actor
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_actor_role
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_actor_type
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_details_title
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_id
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_message
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_metadata
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_resource
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_resource_id
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_resource_sensitivity
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_status
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_timestamp
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.domain.model.event.auditEventMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.audit.detail.AuditEventDetailComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.not_available
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
    val event = state.event
    val notAvailableText = stringResource(Res.string.not_available)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(CoreTheme.dimens.paddingMedium)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
    ) {
        CoreTitleText(
            text = "${stringResource(Res.string.audit_event_id)}: ${event.id.value}",
            style = MaterialTheme.typography.titleMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = CoreTheme.dimens.elevationHeader)
        ) {
            Column(
                modifier = Modifier.padding(CoreTheme.dimens.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
            ) {
                DetailRow(
                    label = stringResource(Res.string.audit_event_action),
                    value = event.action.serialName
                )
                DetailRow(
                    label = stringResource(Res.string.audit_event_status),
                    value = event.status.serialName
                )
                DetailRow(
                    label = stringResource(Res.string.audit_event_resource),
                    value = event.resource.serialName
                )
                event.resourceId?.let { resId ->
                    DetailRow(
                        label = stringResource(Res.string.audit_event_resource_id),
                        value = resId
                    )
                }
                DetailRow(
                    label = stringResource(Res.string.audit_event_resource_sensitivity),
                    value = event.resourceValueSensitivity.name
                )
                DetailRow(
                    label = stringResource(Res.string.audit_event_actor),
                    value = event.actorId ?: notAvailableText
                )
                DetailRow(
                    label = stringResource(Res.string.audit_event_actor_type),
                    value = event.actorType.serialName
                )
                event.actorUserRole?.let { role ->
                    DetailRow(
                        label = stringResource(Res.string.audit_event_actor_role),
                        value = role
                    )
                }
                event.message?.let { msg ->
                    DetailRow(
                        label = stringResource(Res.string.audit_event_message),
                        value = msg
                    )
                }
                val timestampFormatted = formatInstantToDateTime(event.createdAt) ?: event.createdAt.toString()
                DetailRow(
                    label = stringResource(Res.string.audit_event_timestamp),
                    value = timestampFormatted
                )
            }
        }

        if (event.metadata.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = CoreTheme.dimens.elevationHeader)
            ) {
                Column(
                    modifier = Modifier.padding(CoreTheme.dimens.paddingMedium),
                    verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
                ) {
                    CoreTitleText(
                        text = stringResource(Res.string.audit_event_metadata),
                        style = MaterialTheme.typography.titleSmall
                    )

                    HorizontalDivider()

                    event.metadata.forEach { item ->
                        DetailRow(
                            label = item.key.serialName,
                            value = item.value
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        CoreSmallText(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        CoreBodyText(
            text = value,
            modifier = Modifier.weight(1.5f)
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