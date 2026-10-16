package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.main

import com.arkivanov.decompose.ComponentContext

/**
 * Default implementation of [SettingsMainComponent].
 *
 * @param componentContext Decompose [ComponentContext].
 * @param onNavigateToEditAuthSettings Callback to navigate to edit auth settings.
 * @param onNavigateToEditGlobalSettings Callback to navigate to edit global settings.
 * @param onNavigateToEditSecuritySettings Callback to navigate to edit security settings.
 * @param onBack Pops this screen from the navigation stack.
 */
class SettingsMainComponentImpl(
    componentContext: ComponentContext,
    private val onNavigateToEditAuthSettings: () -> Unit,
    private val onNavigateToEditGlobalSettings: () -> Unit,
    private val onNavigateToEditSecuritySettings: () -> Unit,
    private val onBack: () -> Unit
) : SettingsMainComponent, ComponentContext by componentContext {

    override fun onEditAuthSettingsClick() {
        onNavigateToEditAuthSettings()
    }

    override fun onEditGlobalSettingsClick() {
        onNavigateToEditGlobalSettings()
    }

    override fun onEditSecuritySettingsClick() {
        onNavigateToEditSecuritySettings()
    }

    override fun onBackClick() {
        onBack()
    }
}
