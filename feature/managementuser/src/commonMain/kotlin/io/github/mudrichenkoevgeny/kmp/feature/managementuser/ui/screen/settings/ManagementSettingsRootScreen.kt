package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.auth.EditAuthSettingsScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.global.EditGlobalSettingsScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.main.MainManagementSettingsScreen
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.security.EditSecuritySettingsScreen

/**
 * Root Composable for management settings stack.
 *
 * @param component Root Decompose stack component.
 */
@Composable
fun ManagementSettingsRootScreen(component: ManagementSettingsRootComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation(slide())
    ) { child ->
        when (val instance = child.instance) {
            is ManagementSettingsRootComponent.Child.Main -> MainManagementSettingsScreen(instance.component)
            is ManagementSettingsRootComponent.Child.EditAuthSettings -> EditAuthSettingsScreen(instance.component)
            is ManagementSettingsRootComponent.Child.EditGlobalSettings -> EditGlobalSettingsScreen(instance.component)
            is ManagementSettingsRootComponent.Child.EditSecuritySettings -> EditSecuritySettingsScreen(instance.component)
        }
    }
}
