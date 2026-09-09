package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.settings.main

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.main.MainManagementSettingsComponent

@InternalApi
class MainManagementSettingsComponentMock : MainManagementSettingsComponent {
    override fun onEditAuthSettingsClick() {}
    override fun onEditGlobalSettingsClick() {}
    override fun onEditSecuritySettingsClick() {}
    override fun onUsersManagementClick() {}
    override fun onAuditLogsClick() {}
}
