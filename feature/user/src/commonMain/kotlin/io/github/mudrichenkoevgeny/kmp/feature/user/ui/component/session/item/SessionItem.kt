package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.session.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.session_expires_at
import io.github.mudrichenkoevgeny.kmp.feature.user.session_ip_address
import io.github.mudrichenkoevgeny.kmp.feature.user.session_last_accessed
import io.github.mudrichenkoevgeny.kmp.feature.user.session_revoke
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.SessionListTestTags
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import org.jetbrains.compose.resources.stringResource

/**
 * A list item representing an active authenticated user session.
 *
 * Displays device info, IP address, and timestamps for last access and expiration.
 *
 * @param session The [UserSession] data to display.
 * @param onRevokeClick Callback invoked when the revoke button is clicked.
 * @param enabled Whether the revoke action and UI interactions are permitted.
 */
@Composable
fun SessionItem(
    session: UserSession,
    onRevokeClick: () -> Unit,
    enabled: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(SessionListTestTags.SESSION_ITEM_PREFIX + session.id.value),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevationHeader)
    ) {
        Column(modifier = Modifier.padding(Dimens.paddingMedium)) {
            Text(
                text = session.userAgent ?: "Unknown device",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(Dimens.paddingSmall))
            Text(
                text = stringResource(Res.string.session_ip_address, session.ipAddress ?: "Unknown"),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = stringResource(Res.string.session_last_accessed, session.lastAccessedAt.toString()),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = stringResource(Res.string.session_expires_at, session.expiresAt.toString()),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(Dimens.paddingSmall))
            HorizontalDivider()
            Spacer(Modifier.height(Dimens.paddingSmall))
            Button(
                onClick = onRevokeClick,
                modifier = Modifier.align(Alignment.End),
                enabled = enabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text(stringResource(Res.string.session_revoke))
            }
        }
    }
}