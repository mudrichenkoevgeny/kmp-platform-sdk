package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.ic_back
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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.back
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.edit_auth_settings
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.edit_global_settings
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.edit_security_settings
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.management_settings_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

object SettingsMainTestTags {
    const val TITLE = "SettingsMain_Title"
    const val BACK_BUTTON = "SettingsMain_BackButton"
    const val EDIT_AUTH_SETTINGS_BUTTON = "SettingsMain_EditAuthSettingsButton"
    const val EDIT_GLOBAL_SETTINGS_BUTTON = "SettingsMain_EditGlobalSettingsButton"
    const val EDIT_SECURITY_SETTINGS_BUTTON = "SettingsMain_EditSecuritySettingsButton"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMainScreen(component: SettingsMainComponent) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = stringResource(Res.string.management_settings_title),
                        modifier = Modifier.testTag(SettingsMainTestTags.TITLE)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = component::onBackClick,
                        modifier = Modifier.testTag(SettingsMainTestTags.BACK_BUTTON)
                    ) {
                        Icon(
                            painter = painterResource(CommonRes.drawable.ic_back),
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
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
                text = stringResource(Res.string.edit_auth_settings),
                onClick = component::onEditAuthSettingsClick,
                modifier = Modifier.testTag(SettingsMainTestTags.EDIT_AUTH_SETTINGS_BUTTON)
            )

            CoreButton(
                text = stringResource(Res.string.edit_global_settings),
                onClick = component::onEditGlobalSettingsClick,
                modifier = Modifier.testTag(SettingsMainTestTags.EDIT_GLOBAL_SETTINGS_BUTTON)
            )

            CoreButton(
                text = stringResource(Res.string.edit_security_settings),
                onClick = component::onEditSecuritySettingsClick,
                modifier = Modifier.testTag(SettingsMainTestTags.EDIT_SECURITY_SETTINGS_BUTTON)
            )
        }
    }
}

@InternalApi
@Composable
private fun SettingsMainScreenPreviewContent() {
    CompositionLocalProvider(LocalErrorParser provides AppErrorParserMock) {
        SettingsMainScreen(
            component = object : SettingsMainComponent {
                override fun onEditAuthSettingsClick() {}
                override fun onEditGlobalSettingsClick() {}
                override fun onEditSecuritySettingsClick() {}
                override fun onBackClick() {}
            }
        )
    }
}

@InternalApi
@Preview(showBackground = true)
@Composable
private fun SettingsMainScreenPreview() {
    ScreenPreviewContainer {
        SettingsMainScreenPreviewContent()
    }
}

@InternalApi
@ScreenSizePreviews
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        SettingsMainScreenPreviewContent()
    }
}

@InternalApi
@ThemePreviews
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        SettingsMainScreenPreviewContent()
    }
}

@InternalApi
@FontScalePreviews
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        SettingsMainScreenPreviewContent()
    }
}
