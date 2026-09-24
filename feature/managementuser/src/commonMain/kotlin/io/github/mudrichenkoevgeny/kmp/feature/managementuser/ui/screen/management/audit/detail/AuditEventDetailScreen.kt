package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail

import androidx.compose.foundation.clickable
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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_details_title
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_id
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_message
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_metadata
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_resource
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_resource_sensitivity
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_status
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_timestamp
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.domain.model.event.auditEventMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.audit.detail.AuditEventDetailComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.resource.UserAuditResourceType
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
                    Content(
                        state = currentState,
                        onResourceClick = component::onResourceClick,
                        onSubjectClick = component::onSubjectClick
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: AuditEventDetailScreenState.Content,
    onResourceClick: () -> Unit,
    onSubjectClick: () -> Unit
) {
    val event = state.event

    val resourceName = event.resource.serialName
    val isResourceClickable = event.resourceId != null && (
        resourceName.equals(UserAuditResourceType.USER.serialName, ignoreCase = true) ||
            resourceName.equals(UserAuditResourceType.SESSION.serialName, ignoreCase = true) ||
            resourceName.equals(UserAuditResourceType.IDENTIFIER.serialName, ignoreCase = true)
    )
    val isSubjectClickable = !event.actorId.isNullOrBlank() && (
        event.actorType == AuditActorType.USER ||
            event.actorType.serialName.equals(AuditActorType.USER.serialName, ignoreCase = true)
    )

    val resourceValue = if (event.resourceId != null) {
        "${event.resource.serialName}: ${event.resourceId}"
    } else {
        event.resource.serialName
    }

    val subjectValue = buildString {
        append(event.actorType.serialName)
        if (!event.actorUserRole.isNullOrBlank()) {
            append(" (${event.actorUserRole})")
        }
        if (!event.actorId.isNullOrBlank()) {
            append(": ${event.actorId}")
        }
    }

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
                    value = resourceValue,
                    onClick = if (isResourceClickable) onResourceClick else null,
                    modifier = Modifier.testTag(AuditEventDetailTestTags.RESOURCE_ROW)
                )
                DetailRow(
                    label = stringResource(Res.string.audit_event_resource_sensitivity),
                    value = event.resourceValueSensitivity.name
                )
                DetailRow(
                    label = stringResource(Res.string.audit_event_actor),
                    value = subjectValue,
                    onClick = if (isSubjectClickable) onSubjectClick else null,
                    modifier = Modifier.testTag(AuditEventDetailTestTags.SUBJECT_ROW)
                )
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
    value: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
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
            color = if (onClick != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
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
    const val RESOURCE_ROW = "AuditEventDetail_ResourceRow"
    const val SUBJECT_ROW = "AuditEventDetail_SubjectRow"
}