package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.session.item

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
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
        elevation = CardDefaults.cardElevation(defaultElevation = CoreTheme.dimens.elevationHeader)
    ) {
        Column(modifier = Modifier.padding(CoreTheme.dimens.paddingMedium)) {
            Text(
                text = session.userAgent ?: "Unknown device",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
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
            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
            HorizontalDivider()
            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
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

private data class SessionItemPreviewState(
    val session: UserSession,
    val enabled: Boolean
)

@InternalApi
private class SessionItemPreviewProvider : PreviewParameterProvider<SessionItemPreviewState> {
    private val items = listOf(
        SessionItemPreviewState(
            session = userSessionMock(),
            enabled = true
        ),
        SessionItemPreviewState(
            session = userSessionMock().copy(
                userAgent = null,
                ipAddress = null
            ),
            enabled = true
        ),
        SessionItemPreviewState(
            session = userSessionMock(),
            enabled = false
        )
    )

    override val values: Sequence<SessionItemPreviewState> = items.asSequence()
}

@InternalApi
@Composable
private fun SessionItemPreviewContent(state: SessionItemPreviewState) {
    CoreTheme {
        Surface {
            Box(modifier = Modifier.padding(CoreTheme.dimens.paddingLarge)) {
                SessionItem(
                    session = state.session,
                    onRevokeClick = {},
                    enabled = state.enabled
                )
            }
        }
    }
}

@InternalApi
private val defaultSessionItemPreviewState = SessionItemPreviewState(
    session = userSessionMock(),
    enabled = true
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun SessionItemStatesPreview(
    @PreviewParameter(SessionItemPreviewProvider::class) state: SessionItemPreviewState
) {
    SessionItemPreviewContent(state = state)
}

@InternalApi
@ComponentSizePreviews
@Composable
private fun SessionItemComponentSizePreview() {
    SessionItemPreviewContent(state = defaultSessionItemPreviewState)
}

@InternalApi
@ThemePreviews
@Composable
private fun SessionItemThemePreview() {
    SessionItemPreviewContent(state = defaultSessionItemPreviewState)
}

@InternalApi
@FontScalePreviews
@Composable
private fun SessionItemFontScalePreview() {
    SessionItemPreviewContent(state = defaultSessionItemPreviewState)
}