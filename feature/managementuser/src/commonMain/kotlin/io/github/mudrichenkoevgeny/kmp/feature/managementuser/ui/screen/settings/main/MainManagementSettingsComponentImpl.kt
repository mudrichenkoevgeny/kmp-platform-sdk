package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.main

import com.arkivanov.decompose.ComponentContext

/**
 * Default implementation of [MainManagementSettingsComponent].
 */
class MainManagementSettingsComponentImpl(
    componentContext: ComponentContext,
    private val onNavigateToEditAuthSettings: () -> Unit,
    private val onNavigateToEditGlobalSettings: () -> Unit,
    private val onNavigateToEditSecuritySettings: () -> Unit
) : MainManagementSettingsComponent, ComponentContext by componentContext {

    override fun onEditAuthSettingsClick() {
        onNavigateToEditAuthSettings()
    }

    override fun onEditGlobalSettingsClick() {
        onNavigateToEditGlobalSettings()
    }

    override fun onEditSecuritySettingsClick() {
        onNavigateToEditSecuritySettings()
    }
}
