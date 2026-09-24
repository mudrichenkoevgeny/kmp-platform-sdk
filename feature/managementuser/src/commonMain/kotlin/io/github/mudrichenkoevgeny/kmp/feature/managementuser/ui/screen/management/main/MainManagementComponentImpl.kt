package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main

import com.arkivanov.decompose.ComponentContext

/**
 * Default implementation of [MainManagementComponent].
 */
class MainManagementComponentImpl(
    componentContext: ComponentContext,
    private val onNavigateToEditAuthSettings: () -> Unit,
    private val onNavigateToEditGlobalSettings: () -> Unit,
    private val onNavigateToEditSecuritySettings: () -> Unit,
    private val onNavigateToGlobalUserList: () -> Unit,
    private val onNavigateToAuditEventList: () -> Unit,
    private val onNavigateToGlobalSessionList: () -> Unit,
    private val onNavigateToGlobalIdentifierList: () -> Unit
) : MainManagementComponent, ComponentContext by componentContext {

    override fun onEditAuthSettingsClick() {
        onNavigateToEditAuthSettings()
    }

    override fun onEditGlobalSettingsClick() {
        onNavigateToEditGlobalSettings()
    }

    override fun onEditSecuritySettingsClick() {
        onNavigateToEditSecuritySettings()
    }

    override fun onGlobalUserListClick() {
        onNavigateToGlobalUserList()
    }

    override fun onAuditEventListClick() {
        onNavigateToAuditEventList()
    }

    override fun onGlobalSessionListClick() {
        onNavigateToGlobalSessionList()
    }

    override fun onGlobalIdentifierListClick() {
        onNavigateToGlobalIdentifierList()
    }
}