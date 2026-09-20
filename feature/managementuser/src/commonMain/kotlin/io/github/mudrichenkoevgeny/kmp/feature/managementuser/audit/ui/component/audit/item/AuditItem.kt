package io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.component.audit.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_id
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_event_status
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.events.AuditEventsTestTags
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.domain.model.event.auditEventMock
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent
import org.jetbrains.compose.resources.stringResource

/**
 * A list item representing a single audit event record.
 *
 * @param event The [AuditEvent] data to display.
 * @param onClick Callback invoked when the item is clicked.
 * @param modifier Optional [Modifier].
 */
@Composable
fun AuditItem(
    event: AuditEvent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(AuditEventsTestTags.AUDIT_ITEM_PREFIX + event.id.value),
        elevation = CardDefaults.cardElevation(defaultElevation = CoreTheme.dimens.elevationHeader)
    ) {
        Column(modifier = Modifier.padding(CoreTheme.dimens.paddingMedium)) {
            Text(
                text = "${stringResource(Res.string.audit_event_id)}: ${event.id.value}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
            Text(
                text = "${stringResource(Res.string.audit_event_status)}: ${event.status}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@InternalApi
@Composable
private fun AuditItemPreviewContent(event: AuditEvent) {
    CoreTheme {
        Surface {
            Box(modifier = Modifier.padding(CoreTheme.dimens.paddingLarge)) {
                AuditItem(
                    event = event,
                    onClick = {}
                )
            }
        }
    }
}

@InternalApi
private val defaultAuditItemPreviewState = auditEventMock()

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun AuditItemStatesPreview() {
    AuditItemPreviewContent(event = defaultAuditItemPreviewState)
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun AuditItemComponentSizePreview() {
    AuditItemPreviewContent(event = defaultAuditItemPreviewState)
}

@InternalApi
@ThemePreviews
@Composable
private fun AuditItemThemePreview() {
    AuditItemPreviewContent(event = defaultAuditItemPreviewState)
}

@InternalApi
@FontScalePreviews
@Composable
private fun AuditItemFontScalePreview() {
    AuditItemPreviewContent(event = defaultAuditItemPreviewState)
}
