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
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewScreenSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.storage.localization.LocalStorageStrings
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.main.MainManagementComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings

/**
 * Main dashboard screen for management application administrative navigation.
 *
 * @param component Decompose controller for managing main management section choices.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun MainManagementScreen(component: MainManagementComponent) {
    val userUi = LocalUserStrings.current.ui
    val storageUi = LocalStorageStrings.current.ui

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = userUi.usersManagement,
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
                text = userUi.usersManagement,
                onClick = component::onGlobalUserListClick,
                modifier = Modifier.testTag(MainManagementTestTags.GLOBAL_USER_LIST_BUTTON)
            )

            CoreButton(
                text = userUi.sessions,
                onClick = component::onGlobalSessionListClick,
                modifier = Modifier.testTag(MainManagementTestTags.SESSIONS_BUTTON)
            )

            CoreButton(
                text = userUi.identifiers,
                onClick = component::onGlobalIdentifierListClick,
                modifier = Modifier.testTag(MainManagementTestTags.IDENTIFIERS_BUTTON)
            )

            CoreButton(
                text = storageUi.storageFileListTitle,
                onClick = component::onGlobalStorageFileListClick,
                modifier = Modifier.testTag(MainManagementTestTags.GLOBAL_STORAGE_FILE_LIST_BUTTON)
            )

            CoreButton(
                text = userUi.auditLogs,
                onClick = component::onAuditEventListClick,
                modifier = Modifier.testTag(MainManagementTestTags.AUDIT_EVENT_LIST_BUTTON)
            )

            CoreButton(
                text = userUi.managementSettingsTitle,
                onClick = component::onSettingsClick,
                modifier = Modifier.testTag(MainManagementTestTags.SETTINGS_BUTTON)
            )
        }
    }
}

@InternalApi
@Composable
private fun MainManagementScreenPreviewContent() {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        MainManagementScreen(
            component = MainManagementComponentMock()
        )
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun MainManagementScreenPreview() {
    ScreenPreviewContainer {
        MainManagementScreenPreviewContent()
    }
}

@InternalApi
@PreviewScreenSizes
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        MainManagementScreenPreviewContent()
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        MainManagementScreenPreviewContent()
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        MainManagementScreenPreviewContent()
    }
}

public object MainManagementTestTags {
    public const val TITLE: String = "MainManagement_Title"
    public const val GLOBAL_USER_LIST_BUTTON: String = "MainManagement_GlobalUserListButton"
    public const val SESSIONS_BUTTON: String = "MainManagement_SessionsButton"
    public const val IDENTIFIERS_BUTTON: String = "MainManagement_IdentifiersButton"
    public const val AUDIT_EVENT_LIST_BUTTON: String = "MainManagement_AuditEventListButton"
    public const val SETTINGS_BUTTON: String = "MainManagement_SettingsButton"
    public const val GLOBAL_STORAGE_FILE_LIST_BUTTON: String = "MainManagement_GlobalStorageFileListButton"
}
