package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.component.audit.item

import androidx.compose.foundation.clickable
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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.Res
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.audit_event_id
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.audit_event_status
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.events.AuditEventsTestTags
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.action.UserAuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.resource.UserAuditResourceType
import kotlin.time.Instant
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
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevationHeader)
    ) {
        Column(modifier = Modifier.padding(Dimens.paddingMedium)) {
            Text(
                text = "${stringResource(Res.string.audit_event_id)}: ${event.id.value}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(Dimens.paddingSmall))
            Text(
                text = "${stringResource(Res.string.audit_event_status)}: ${event.status}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun AuditItemPreview() {
    MaterialTheme {
        Surface {
            AuditItem(
                event = AuditEvent(
                    actorType = AuditActorType.USER,
                    action = UserAuditActionType.MANAGEMENT_UPDATE_USER,
                    resource = UserAuditResourceType.USER,
                    status = AuditStatus.SUCCESS,
                    createdAt = Instant.fromEpochMilliseconds(0)
                ),
                onClick = {}
            )
        }
    }
}
