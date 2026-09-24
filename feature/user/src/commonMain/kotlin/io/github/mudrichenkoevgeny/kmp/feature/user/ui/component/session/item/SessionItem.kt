package io.github.mudrichenkoevgeny.kmp.feature.user.ui.component.session.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.time.formatInstantToDateTime
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ComponentSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.auth_logo_apple
import io.github.mudrichenkoevgeny.kmp.feature.user.auth_logo_email
import io.github.mudrichenkoevgeny.kmp.feature.user.auth_logo_google
import io.github.mudrichenkoevgeny.kmp.feature.user.auth_logo_phone
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.kmp.feature.user.session_ip_address
import io.github.mudrichenkoevgeny.kmp.feature.user.session_last_accessed
import io.github.mudrichenkoevgeny.kmp.feature.user.session_revoke
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * A list item representing an active authenticated user session.
 *
 * Displays device info, IP address, and timestamps for last access and expiration.
 *
 * @param session The [UserSession] data to display.
 * @param onRevokeClick Callback invoked when the revoke button is clicked.
 * @param enabled Whether the revoke action and UI interactions are permitted.
 * @param isCurrentSession Indicates if this session is the current device session.
 * @param onSessionClick Optional callback invoked when the item card is tapped.
 */
@Composable
fun SessionItem(
    session: UserSession,
    onRevokeClick: () -> Unit,
    enabled: Boolean,
    isCurrentSession: Boolean = false,
    onSessionClick: (() -> Unit)? = null
) {
    val cardColors = if (isCurrentSession) {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    } else {
        CardDefaults.cardColors()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onSessionClick != null) {
                    Modifier.clickable { onSessionClick() }
                } else {
                    Modifier
                }
            )
            .testTag(SessionItemTestTags.ITEM_PREFIX + session.id.value),
        elevation = CardDefaults.cardElevation(defaultElevation = CoreTheme.dimens.elevationHeader),
        colors = cardColors
    ) {
        Column(modifier = Modifier.padding(CoreTheme.dimens.paddingMedium)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val iconRes = when (session.identifierAuthProvider) {
                    UserAuthProvider.EMAIL -> Res.drawable.auth_logo_email
                    UserAuthProvider.PHONE -> Res.drawable.auth_logo_phone
                    UserAuthProvider.GOOGLE -> Res.drawable.auth_logo_google
                    UserAuthProvider.APPLE -> Res.drawable.auth_logo_apple
                }
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(CoreTheme.dimens.paddingSmall))
                Text(
                    text = session.identifierDisplayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
            Text(
                text = session.deviceInfo.deviceName ?: session.userAgent ?: "Unknown device",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            session.deviceInfo.clientType?.name?.let { clientTypeName ->
                Spacer(Modifier.height(CoreTheme.dimens.paddingExtraSmall))
                Text(
                    text = clientTypeName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(Modifier.height(CoreTheme.dimens.paddingSmall))
            Text(
                text = stringResource(Res.string.session_ip_address, session.ipAddress ?: "Unknown"),
                style = MaterialTheme.typography.bodySmall
            )
            val formattedDate = formatInstantToDateTime(session.lastAccessedAt) ?: session.lastAccessedAt.toString()
            Text(
                text = stringResource(Res.string.session_last_accessed, formattedDate),
                style = MaterialTheme.typography.bodySmall
            )

            if (!isCurrentSession) {
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
}

private data class SessionItemPreviewState(
    val session: UserSession,
    val enabled: Boolean,
    val isCurrentSession: Boolean = false
)

@InternalApi
private class SessionItemPreviewProvider : PreviewParameterProvider<SessionItemPreviewState> {
    private val items: List<Pair<String, SessionItemPreviewState>> = listOf(
        "Default" to SessionItemPreviewState(
            session = userSessionMock(),
            enabled = true
        ),
        "Unknown Device and IP" to SessionItemPreviewState(
            session = userSessionMock().copy(
                userAgent = null,
                ipAddress = null
            ),
            enabled = true
        ),
        "Disabled Action" to SessionItemPreviewState(
            session = userSessionMock(),
            enabled = false
        ),
        "Current Session" to SessionItemPreviewState(
            session = userSessionMock(),
            enabled = true,
            isCurrentSession = true
        )
    )

    override val values: Sequence<SessionItemPreviewState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
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
                    enabled = state.enabled,
                    isCurrentSession = state.isCurrentSession
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

object SessionItemTestTags {
    const val ITEM_PREFIX = "SessionItem_"
}