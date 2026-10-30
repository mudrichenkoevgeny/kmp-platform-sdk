package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.main

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main.MainManagementComponent

@InternalApi
open class MainManagementComponentMock : MainManagementComponent {
    override fun onGlobalUserListClick() {}
    override fun onGlobalSessionListClick() {}
    override fun onGlobalIdentifierListClick() {}
    override fun onAuditEventListClick() {}
    override fun onSettingsClick() {}
    override fun onGlobalStorageFileListClick() {}
}
