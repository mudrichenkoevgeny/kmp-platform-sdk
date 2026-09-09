package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.Res
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.*
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
                    Text(
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
                .padding(Dimens.paddingMedium)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
        ) {
            Button(
                onClick = component::onUsersManagementClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(MainManagementSettingsTestTags.USERS_MANAGEMENT_BUTTON)
            ) {
                Text(text = stringResource(Res.string.users_management))
            }

            Button(
                onClick = component::onAuditLogsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(MainManagementSettingsTestTags.AUDIT_LOGS_BUTTON)
            ) {
                Text(text = stringResource(Res.string.audit_logs))
            }

            Button(
                onClick = component::onEditAuthSettingsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(MainManagementSettingsTestTags.EDIT_AUTH_SETTINGS_BUTTON)
            ) {
                Text(text = stringResource(Res.string.edit_auth_settings))
            }

            Button(
                onClick = component::onEditGlobalSettingsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(MainManagementSettingsTestTags.EDIT_GLOBAL_SETTINGS_BUTTON)
            ) {
                Text(text = stringResource(Res.string.edit_global_settings))
            }

            Button(
                onClick = component::onEditSecuritySettingsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(MainManagementSettingsTestTags.EDIT_SECURITY_SETTINGS_BUTTON)
            ) {
                Text(text = stringResource(Res.string.edit_security_settings))
            }
        }
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun MainManagementSettingsPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimens.paddingMedium),
                    verticalArrangement = Arrangement.spacedBy(Dimens.paddingMedium)
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(MainManagementSettingsTestTags.USERS_MANAGEMENT_BUTTON)
                    ) {
                        Text(text = stringResource(Res.string.users_management))
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(MainManagementSettingsTestTags.AUDIT_LOGS_BUTTON)
                    ) {
                        Text(text = stringResource(Res.string.audit_logs))
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(MainManagementSettingsTestTags.EDIT_AUTH_SETTINGS_BUTTON)
                    ) {
                        Text(text = stringResource(Res.string.edit_auth_settings))
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(MainManagementSettingsTestTags.EDIT_GLOBAL_SETTINGS_BUTTON)
                    ) {
                        Text(text = stringResource(Res.string.edit_global_settings))
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(MainManagementSettingsTestTags.EDIT_SECURITY_SETTINGS_BUTTON)
                    ) {
                        Text(text = stringResource(Res.string.edit_security_settings))
                    }
                }
            }
        }
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
