package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main

import com.arkivanov.decompose.ComponentContext

/**
 * Default implementation of [MainManagementComponent].
 */
class MainManagementComponentImpl(
    componentContext: ComponentContext,
    private val onNavigateToGlobalUserList: () -> Unit,
    private val onNavigateToGlobalSessionList: () -> Unit,
    private val onNavigateToGlobalIdentifierList: () -> Unit,
    private val onNavigateToGlobalStorageFileList: () -> Unit,
    private val onNavigateToAuditEventList: () -> Unit,
    private val onNavigateToSettings: () -> Unit
) : MainManagementComponent, ComponentContext by componentContext {

    override fun onGlobalUserListClick() {
        onNavigateToGlobalUserList()
    }

    override fun onGlobalSessionListClick() {
        onNavigateToGlobalSessionList()
    }

    override fun onGlobalIdentifierListClick() {
        onNavigateToGlobalIdentifierList()
    }

    override fun onGlobalStorageFileListClick() {
        onNavigateToGlobalStorageFileList()
    }

    override fun onAuditEventListClick() {
        onNavigateToAuditEventList()
    }

    override fun onSettingsClick() {
        onNavigateToSettings()
    }
}
