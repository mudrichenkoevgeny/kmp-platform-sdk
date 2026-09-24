package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail

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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.user.Res
import io.github.mudrichenkoevgeny.kmp.feature.user.auth_logo_apple
import io.github.mudrichenkoevgeny.kmp.feature.user.auth_logo_email
import io.github.mudrichenkoevgeny.kmp.feature.user.auth_logo_google
import io.github.mudrichenkoevgeny.kmp.feature.user.auth_logo_phone
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.session.detail.SessionDetailComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.not_available
import io.github.mudrichenkoevgeny.kmp.feature.user.session_detail_app_version
import io.github.mudrichenkoevgeny.kmp.feature.user.session_detail_client_type
import io.github.mudrichenkoevgeny.kmp.feature.user.session_detail_created_at_label
import io.github.mudrichenkoevgeny.kmp.feature.user.session_detail_device_info
import io.github.mudrichenkoevgeny.kmp.feature.user.session_detail_device_name
import io.github.mudrichenkoevgeny.kmp.feature.user.session_detail_ip_address_label
import io.github.mudrichenkoevgeny.kmp.feature.user.session_detail_language
import io.github.mudrichenkoevgeny.kmp.feature.user.session_detail_last_accessed_label
import io.github.mudrichenkoevgeny.kmp.feature.user.session_detail_os_version
import io.github.mudrichenkoevgeny.kmp.feature.user.session_detail_title_current_session
import io.github.mudrichenkoevgeny.kmp.feature.user.session_detail_title_session
import io.github.mudrichenkoevgeny.kmp.feature.user.session_revoke
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionDetailScreen(component: SessionDetailComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val currentContent = state as? SessionDetailScreenState.Content
                    val titleRes = if (currentContent?.isCurrentSession == true) {
                        Res.string.session_detail_title_current_session
                    } else {
                        Res.string.session_detail_title_session
                    }
                    CoreScreenTitleText(
                        text = stringResource(titleRes),
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
                        onRevokeClick = component::onRevokeSessionClick
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
    onRevokeClick: () -> Unit
) {
    val session = state.session
    val notAvailableText = stringResource(Res.string.not_available)

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
                            text = session.identifierAuthProvider.name,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.testTag(SessionDetailTestTags.AUTH_PROVIDER)
                        )
                    }
                }

                HorizontalDivider()

                CoreTitleText(
                    text = stringResource(Res.string.session_detail_device_info),
                    style = MaterialTheme.typography.titleSmall
                )
                CoreBodyText(
                    text = "${stringResource(Res.string.session_detail_device_name)}: ${session.deviceInfo.deviceName ?: session.userAgent ?: notAvailableText}",
                    modifier = Modifier.testTag(SessionDetailTestTags.DEVICE_NAME)
                )
                CoreBodyText(
                    text = "${stringResource(Res.string.session_detail_client_type)}: ${session.deviceInfo.clientType?.name ?: notAvailableText}",
                    modifier = Modifier.testTag(SessionDetailTestTags.CLIENT_TYPE)
                )
                CoreBodyText(
                    text = "${stringResource(Res.string.session_detail_language)}: ${session.deviceInfo.language ?: notAvailableText}",
                    modifier = Modifier.testTag(SessionDetailTestTags.LANGUAGE)
                )
                CoreBodyText(
                    text = "${stringResource(Res.string.session_detail_app_version)}: ${session.deviceInfo.appVersion ?: notAvailableText}",
                    modifier = Modifier.testTag(SessionDetailTestTags.APP_VERSION)
                )
                CoreBodyText(
                    text = "${stringResource(Res.string.session_detail_os_version)}: ${session.deviceInfo.operationSystemVersion ?: notAvailableText}",
                    modifier = Modifier.testTag(SessionDetailTestTags.OS_VERSION)
                )

                HorizontalDivider()

                CoreBodyText(
                    text = "${stringResource(Res.string.session_detail_ip_address_label)}: ${session.ipAddress ?: notAvailableText}",
                    modifier = Modifier.testTag(SessionDetailTestTags.IP_ADDRESS)
                )

                val lastAccessedFormatted = formatInstantToDateTime(session.lastAccessedAt)
                    ?: session.lastAccessedAt.toString()
                CoreBodyText(
                    text = "${stringResource(Res.string.session_detail_last_accessed_label)}: $lastAccessedFormatted",
                    modifier = Modifier.testTag(SessionDetailTestTags.LAST_ACCESSED_AT)
                )

                val createdAtFormatted = formatInstantToDateTime(session.createdAt)
                    ?: session.createdAt.toString()
                CoreBodyText(
                    text = "${stringResource(Res.string.session_detail_created_at_label)}: $createdAtFormatted",
                    modifier = Modifier.testTag(SessionDetailTestTags.CREATED_AT)
                )
            }
        }

        if (!state.isCurrentSession) {
            CoreButton(
                text = stringResource(Res.string.session_revoke),
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
            session = userSessionMock(),
            isCurrentSession = false
        ),
        "Current Session Content" to SessionDetailScreenState.Content(
            session = userSessionMock(),
            isCurrentSession = true
        ),
        "Action Loading" to SessionDetailScreenState.Content(
            session = userSessionMock(),
            isCurrentSession = false,
            actionLoading = true
        ),
        "Action Error" to SessionDetailScreenState.Content(
            session = userSessionMock(),
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
    session = userSessionMock(),
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
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        SessionDetailScreenPreviewContent(state = defaultSessionDetailPreviewState)
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        SessionDetailScreenPreviewContent(state = defaultSessionDetailPreviewState)
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        SessionDetailScreenPreviewContent(state = defaultSessionDetailPreviewState)
    }
}

object SessionDetailTestTags {
    const val TITLE = "SessionDetail_Title"
    const val BACK_BUTTON = "SessionDetail_BackButton"
    const val GLOBAL_ERROR = "SessionDetail_GlobalError"
    const val SESSION_CARD = "SessionDetail_SessionCard"
    const val IDENTIFIER_DISPLAY_NAME = "SessionDetail_IdentifierDisplayName"
    const val AUTH_PROVIDER = "SessionDetail_AuthProvider"
    const val DEVICE_NAME = "SessionDetail_DeviceName"
    const val CLIENT_TYPE = "SessionDetail_ClientType"
    const val LANGUAGE = "SessionDetail_Language"
    const val APP_VERSION = "SessionDetail_AppVersion"
    const val OS_VERSION = "SessionDetail_OsVersion"
    const val IP_ADDRESS = "SessionDetail_IpAddress"
    const val LAST_ACCESSED_AT = "SessionDetail_LastAccessedAt"
    const val CREATED_AT = "SessionDetail_CreatedAt"
    const val REVOKE_BUTTON = "SessionDetail_RevokeButton"
    const val ACTION_ERROR_TEXT = "SessionDetail_ActionErrorText"
}
