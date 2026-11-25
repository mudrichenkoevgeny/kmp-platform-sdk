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
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.AppErrorParserMock
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button.CoreButton
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text.CoreScreenTitleText
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewFontScales
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewScreenSizes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.PreviewThemes
import io.github.mudrichenkoevgeny.kmp.core.common.ui.preview.ScreenPreviewContainer
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.CoreTheme
import io.github.mudrichenkoevgeny.kmp.core.generated.resources.ic_back
import io.github.mudrichenkoevgeny.kmp.core.storage.localization.LocalStorageStrings
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import org.jetbrains.compose.resources.painterResource
import io.github.mudrichenkoevgeny.kmp.core.generated.resources.Res as CommonRes

/**
 * Main management settings navigation screen.
 *
 * @param component Decompose controller for settings main navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun SettingsMainScreen(
    component: SettingsMainComponent,
    modifier: Modifier = Modifier
) {
    val userUi = LocalUserStrings.current.ui
    val storageUi = LocalStorageStrings.current.ui
    val commonUi = LocalCommonStrings.current.ui

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    CoreScreenTitleText(
                        text = userUi.managementSettingsTitle,
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
                            contentDescription = commonUi.back
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
                text = userUi.editAuthSettings,
                onClick = component::onEditAuthSettingsClick,
                modifier = Modifier.testTag(SettingsMainTestTags.EDIT_AUTH_SETTINGS_BUTTON)
            )

            CoreButton(
                text = userUi.editGlobalSettings,
                onClick = component::onEditGlobalSettingsClick,
                modifier = Modifier.testTag(SettingsMainTestTags.EDIT_GLOBAL_SETTINGS_BUTTON)
            )

            CoreButton(
                text = userUi.editSecuritySettings,
                onClick = component::onEditSecuritySettingsClick,
                modifier = Modifier.testTag(SettingsMainTestTags.EDIT_SECURITY_SETTINGS_BUTTON)
            )

            CoreButton(
                text = storageUi.storageSettingsTitle,
                onClick = component::onEditStorageSettingsClick,
                modifier = Modifier.testTag(SettingsMainTestTags.EDIT_STORAGE_SETTINGS_BUTTON)
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
                override fun onEditAuthSettingsClick() = Unit
                override fun onEditGlobalSettingsClick() = Unit
                override fun onEditSecuritySettingsClick() = Unit
                override fun onEditStorageSettingsClick() = Unit
                override fun onBackClick() = Unit
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
@PreviewScreenSizes
@Composable
private fun ScreenSizePreview() {
    ScreenPreviewContainer {
        SettingsMainScreenPreviewContent()
    }
}

@InternalApi
@PreviewThemes
@Composable
private fun ThemePreview() {
    ScreenPreviewContainer {
        SettingsMainScreenPreviewContent()
    }
}

@InternalApi
@PreviewFontScales
@Composable
private fun FontScalePreview() {
    ScreenPreviewContainer {
        SettingsMainScreenPreviewContent()
    }
}

public object SettingsMainTestTags {
    public const val TITLE: String = "SettingsMain_Title"
    public const val BACK_BUTTON: String = "SettingsMain_BackButton"
    public const val EDIT_AUTH_SETTINGS_BUTTON: String = "SettingsMain_EditAuthSettingsButton"
    public const val EDIT_GLOBAL_SETTINGS_BUTTON: String = "SettingsMain_EditGlobalSettingsButton"
    public const val EDIT_SECURITY_SETTINGS_BUTTON: String = "SettingsMain_EditSecuritySettingsButton"
    public const val EDIT_STORAGE_SETTINGS_BUTTON: String = "SettingsMain_EditStorageSettingsButton"
}
