package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.main

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.*
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.settings.main.MainManagementSettingsComponentMock
import org.jetbrains.compose.resources.stringResource

/**
 * Main management settings screen offering access to edit auth, global, and security settings.
 *
 * @param component Decompose controller for navigation callbacks.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainManagementSettingsScreen(component: MainManagementSettingsComponent) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.management_settings_title),
                        modifier = Modifier.testTag(MainManagementSettingsTestTags.TITLE)
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
                onClick = component::onUsersManagementClick,
                modifier = Modifier.testTag(MainManagementSettingsTestTags.USERS_MANAGEMENT_BUTTON)
            )

            CoreButton(
                text = stringResource(Res.string.audit_logs),
                onClick = component::onAuditLogsClick,
                modifier = Modifier.testTag(MainManagementSettingsTestTags.AUDIT_LOGS_BUTTON)
            )

            CoreButton(
                text = stringResource(Res.string.edit_auth_settings),
                onClick = component::onEditAuthSettingsClick,
                modifier = Modifier.testTag(MainManagementSettingsTestTags.EDIT_AUTH_SETTINGS_BUTTON)
            )

            CoreButton(
                text = stringResource(Res.string.edit_global_settings),
                onClick = component::onEditGlobalSettingsClick,
                modifier = Modifier.testTag(MainManagementSettingsTestTags.EDIT_GLOBAL_SETTINGS_BUTTON)
            )

            CoreButton(
                text = stringResource(Res.string.edit_security_settings),
                onClick = component::onEditSecuritySettingsClick,
                modifier = Modifier.testTag(MainManagementSettingsTestTags.EDIT_SECURITY_SETTINGS_BUTTON)
            )
        }
    }
}

@InternalApi
@Composable
private fun MainManagementSettingsPreviewContent() {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        MainManagementSettingsScreen(
            component = MainManagementSettingsComponentMock()
        )
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun MainManagementSettingsPreview() {
    ScreenPreviewContainer {
        MainManagementSettingsPreviewContent()
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        MainManagementSettingsPreviewContent()
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        MainManagementSettingsPreviewContent()
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        MainManagementSettingsPreviewContent()
    }
}

object MainManagementSettingsTestTags {
    const val TITLE = "MainManagementSettings_Title"
    const val USERS_MANAGEMENT_BUTTON = "MainManagementSettings_UsersManagementButton"
    const val AUDIT_LOGS_BUTTON = "MainManagementSettings_AuditLogsButton"
    const val EDIT_AUTH_SETTINGS_BUTTON = "MainManagementSettings_EditAuthSettingsButton"
    const val EDIT_GLOBAL_SETTINGS_BUTTON = "MainManagementSettings_EditGlobalSettingsButton"
    const val EDIT_SECURITY_SETTINGS_BUTTON = "MainManagementSettings_EditSecuritySettingsButton"
}
