package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.error.parser.toLocalizedMessage
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.time.formatInstantToDateTime
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreBackButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.container.CoreScrollableScreenContent
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.error.FullscreenError
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoading
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreBodyText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreErrorText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreSmallText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewScreenSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.auth_logo_apple
import io.github.mudrichenkoevgeny.kmp.feature.user.auth_logo_email
import io.github.mudrichenkoevgeny.kmp.feature.user.auth_logo_google
import io.github.mudrichenkoevgeny.kmp.feature.user.auth_logo_phone
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.session.detail.SessionDetailComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionPrivate
import org.jetbrains.compose.resources.painterResource

/**
 * Screen displaying detailed information about a specific user session, with actions to revoke session.
 *
 * @param component Component driving session detail state and actions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun SessionDetailScreen(component: SessionDetailComponent) {
    val state by component.state.subscribeAsState()
    val userUi = LocalUserStrings.current.ui

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = if ((state as? SessionDetailScreenState.Content)?.isCurrentSession == true) {
                            userUi.sessionDetailTitleCurrentSession
                        } else {
                            userUi.sessionDetailTitleSession
                        },
                        modifier = Modifier.testTag(SessionDetailTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    CoreBackButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(SessionDetailTestTags.BACK_BUTTON)
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
                is SessionDetailScreenState.Loading -> FullscreenLoading()
                is SessionDetailScreenState.Error -> FullscreenError(
                    error = currentState.error,
                    onRetry = component::onRetry,
                    modifier = Modifier.testTag(SessionDetailTestTags.GLOBAL_ERROR)
                )

                is SessionDetailScreenState.Content -> {
                    Content(
                        state = currentState,
                        onIdentifierClick = component::onIdentifierClick,
                        onRevokeClick = component::onRevokeSessionClick,
                        onUserClick = component::onUserClick
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: SessionDetailScreenState.Content,
    onIdentifierClick: () -> Unit,
    onRevokeClick: () -> Unit,
    onUserClick: () -> Unit
) {
    val session = state.session
    val userUi = LocalUserStrings.current.ui
    val commonUi = LocalCommonStrings.current.ui
    val notAvailableText = commonUi.lockoutNone

    CoreScrollableScreenContent {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(SessionDetailTestTags.SESSION_CARD),
            elevation = CardDefaults.cardElevation(defaultElevation = CoreTheme.dimens.elevationHeader)
        ) {
            Column(
                modifier = Modifier.padding(CoreTheme.dimens.paddingMedium),
                verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingSmall)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onUserClick() }
                        .testTag(SessionDetailTestTags.USER_ROW),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${commonUi.userId}: ${session.userId.asHexDashString()}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag(SessionDetailTestTags.USER_ID)
                        )
                    }
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onIdentifierClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = session.identifierDisplayName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag(SessionDetailTestTags.IDENTIFIER_DISPLAY_NAME)
                        )
                        CoreSmallText(
                            text = session.identifierAuthProvider.value,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.testTag(SessionDetailTestTags.AUTH_PROVIDER)
                        )
                    }
                }

                HorizontalDivider()

                CoreTitleText(
                    text = userUi.sessionDetailDeviceInfo,
                    style = MaterialTheme.typography.titleSmall
                )
                val deviceName = session.clientDeviceInfo.deviceName
                    ?: session.userAgent
                    ?: notAvailableText
                CoreBodyText(
                    text = "${userUi.sessionDetailDeviceName}: $deviceName",
                    modifier = Modifier.testTag(SessionDetailTestTags.DEVICE_NAME)
                )
                val clientType = session.clientDeviceInfo.clientType?.value
                    ?: notAvailableText
                CoreBodyText(
                    text = "${userUi.sessionDetailClientType}: $clientType",
                    modifier = Modifier.testTag(SessionDetailTestTags.CLIENT_TYPE)
                )
                val language = session.clientDeviceInfo.language
                    ?: notAvailableText
                CoreBodyText(
                    text = "${userUi.sessionDetailLanguage}: $language",
                    modifier = Modifier.testTag(SessionDetailTestTags.LANGUAGE)
                )
                val appVersion = session.clientDeviceInfo.appVersion
                    ?: notAvailableText
                CoreBodyText(
                    text = "${userUi.sessionDetailAppVersion}: $appVersion",
                    modifier = Modifier.testTag(SessionDetailTestTags.APP_VERSION)
                )
                val osVersion = session.clientDeviceInfo.operationSystemVersion
                    ?: notAvailableText
                CoreBodyText(
                    text = "${userUi.sessionDetailOsVersion}: $osVersion",
                    modifier = Modifier.testTag(SessionDetailTestTags.OS_VERSION)
                )

                HorizontalDivider()

                CoreBodyText(
                    text = "${userUi.sessionDetailIpAddressLabel}: ${session.ipAddress ?: notAvailableText}",
                    modifier = Modifier.testTag(SessionDetailTestTags.IP_ADDRESS)
                )

                val lastAccessedFormatted = formatInstantToDateTime(session.lastAccessedAt)
                    ?: session.lastAccessedAt.toString()
                CoreBodyText(
                    text = "${userUi.sessionDetailLastAccessedLabel}: $lastAccessedFormatted",
                    modifier = Modifier.testTag(SessionDetailTestTags.LAST_ACCESSED_AT)
                )

                val createdAtFormatted = formatInstantToDateTime(session.createdAt)
                    ?: session.createdAt.toString()
                CoreBodyText(
                    text = "${userUi.sessionDetailCreatedAtLabel}: $createdAtFormatted",
                    modifier = Modifier.testTag(SessionDetailTestTags.CREATED_AT)
                )
            }
        }

        if (!state.isCurrentSession && !state.isSelfSession) {
            CoreButton(
                text = userUi.sessionRevoke,
                onClick = onRevokeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(SessionDetailTestTags.REVOKE_BUTTON),
                enabled = !state.actionLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            )

            state.actionError?.let { error ->
                CoreErrorText(
                    text = error.toLocalizedMessage(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(SessionDetailTestTags.ACTION_ERROR_TEXT)
                )
            }
        }
    }
}

@InternalApi
internal class SessionDetailPreviewProvider : PreviewParameterProvider<SessionDetailScreenState> {
    private val items: List<Pair<String, SessionDetailScreenState>> = listOf(
        "Other Session Content" to SessionDetailScreenState.Content(
            session = createTestUserSessionPrivate(),
            isCurrentSession = false
        ),
        "Current Session Content" to SessionDetailScreenState.Content(
            session = createTestUserSessionPrivate(),
            isCurrentSession = true
        ),
        "Action Loading" to SessionDetailScreenState.Content(
            session = createTestUserSessionPrivate(),
            isCurrentSession = false,
            actionLoading = true
        ),
        "Action Error" to SessionDetailScreenState.Content(
            session = createTestUserSessionPrivate(),
            isCurrentSession = false,
            actionError = CommonError.Unknown()
        ),
        "Error State" to SessionDetailScreenState.Error(error = CommonError.Unknown()),
        "Loading State" to SessionDetailScreenState.Loading
    )

    override val values: Sequence<SessionDetailScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}

@InternalApi
@Composable
private fun SessionDetailScreenPreviewContent(state: SessionDetailScreenState) {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        SessionDetailScreen(
            component = SessionDetailComponentMock(initialState = state)
        )
    }
}

@InternalApi
private val defaultSessionDetailPreviewState = SessionDetailScreenState.Content(
    session = createTestUserSessionPrivate(),
    isCurrentSession = false
)

@InternalApi
@Preview(showBackground = true, group = "States")
@Composable
private fun StatesPreview(
    @PreviewParameter(SessionDetailPreviewProvider::class) state: SessionDetailScreenState
) {
    ScreenPreviewContainer {
        SessionDetailScreenPreviewContent(state = state)
    }
}

@InternalApi
@PreviewScreenSizes
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        SessionDetailScreenPreviewContent(state = defaultSessionDetailPreviewState)
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        SessionDetailScreenPreviewContent(state = defaultSessionDetailPreviewState)
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        SessionDetailScreenPreviewContent(state = defaultSessionDetailPreviewState)
    }
}

public object SessionDetailTestTags {
    public const val TITLE: String = "SessionDetail_Title"
    public const val BACK_BUTTON: String = "SessionDetail_BackButton"
    public const val GLOBAL_ERROR: String = "SessionDetail_GlobalError"
    public const val USER_ROW: String = "SessionDetail_UserRow"
    public const val USER_ID: String = "SessionDetail_UserId"
    public const val SESSION_CARD: String = "SessionDetail_SessionCard"
    public const val IDENTIFIER_DISPLAY_NAME: String = "SessionDetail_IdentifierDisplayName"
    public const val AUTH_PROVIDER: String = "SessionDetail_AuthProvider"
    public const val DEVICE_NAME: String = "SessionDetail_DeviceName"
    public const val CLIENT_TYPE: String = "SessionDetail_ClientType"
    public const val LANGUAGE: String = "SessionDetail_Language"
    public const val APP_VERSION: String = "SessionDetail_AppVersion"
    public const val OS_VERSION: String = "SessionDetail_OsVersion"
    public const val IP_ADDRESS: String = "SessionDetail_IpAddress"
    public const val LAST_ACCESSED_AT: String = "SessionDetail_LastAccessedAt"
    public const val CREATED_AT: String = "SessionDetail_CreatedAt"
    public const val REVOKE_BUTTON: String = "SessionDetail_RevokeButton"
    public const val ACTION_ERROR_TEXT: String = "SessionDetail_ActionErrorText"
}
