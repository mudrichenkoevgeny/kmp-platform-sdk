package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.FontScalePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenSizePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ThemePreviews
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit_logs
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.edit_auth_settings
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.edit_global_settings
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.edit_security_settings
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.management_settings_title
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.main.MainManagementComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.sessions
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.users_management
import org.jetbrains.compose.resources.stringResource

/**
 * Main management screen offering access to edit auth, global, and security settings.
 *
 * @param component Decompose controller for navigation callbacks.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainManagementScreen(component: MainManagementComponent) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.management_settings_title),
                        modifier = Modifier.testTag(MainManagementTestTags.TITLE)
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(CoreTheme.dimens.paddingMedium)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(CoreTheme.dimens.paddingMedium)
        ) {
            CoreButton(
                text = stringResource(Res.string.users_management),
                onClick = component::onGlobalUserListClick,
                modifier = Modifier.testTag(MainManagementTestTags.GLOBAL_USER_LIST_BUTTON)
            )

            CoreButton(
                text = stringResource(Res.string.sessions),
                onClick = component::onGlobalSessionListClick,
                modifier = Modifier.testTag(MainManagementTestTags.SESSIONS_BUTTON)
            )

            CoreButton(
                text = stringResource(Res.string.audit_logs),
                onClick = component::onAuditEventListClick,
                modifier = Modifier.testTag(MainManagementTestTags.AUDIT_EVENT_LIST_BUTTON)
            )

            CoreButton(
                text = stringResource(Res.string.edit_auth_settings),
                onClick = component::onEditAuthSettingsClick,
                modifier = Modifier.testTag(MainManagementTestTags.EDIT_AUTH_SETTINGS_BUTTON)
            )

            CoreButton(
                text = stringResource(Res.string.edit_global_settings),
                onClick = component::onEditGlobalSettingsClick,
                modifier = Modifier.testTag(MainManagementTestTags.EDIT_GLOBAL_SETTINGS_BUTTON)
            )

            CoreButton(
                text = stringResource(Res.string.edit_security_settings),
                onClick = component::onEditSecuritySettingsClick,
                modifier = Modifier.testTag(MainManagementTestTags.EDIT_SECURITY_SETTINGS_BUTTON)
            )
        }
    }
}

@InternalApi
@Composable
private fun MainManagementPreviewContent() {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        MainManagementScreen(
            component = MainManagementComponentMock()
        )
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun MainManagementPreview() {
    ScreenPreviewContainer {
        MainManagementPreviewContent()
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        MainManagementPreviewContent()
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        MainManagementPreviewContent()
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        MainManagementPreviewContent()
    }
}

object MainManagementTestTags {
    const val TITLE = "MainManagement_Title"
    const val GLOBAL_USER_LIST_BUTTON = "MainManagement_GlobalUserListButton"
    const val AUDIT_EVENT_LIST_BUTTON = "MainManagement_AuditEventListButton"
    const val EDIT_AUTH_SETTINGS_BUTTON = "MainManagement_EditAuthSettingsButton"
    const val EDIT_GLOBAL_SETTINGS_BUTTON = "MainManagement_EditGlobalSettingsButton"
    const val EDIT_SECURITY_SETTINGS_BUTTON = "MainManagement_EditSecuritySettingsButton"
    const val SESSIONS_BUTTON = "MainManagement_SessionsButton"
}