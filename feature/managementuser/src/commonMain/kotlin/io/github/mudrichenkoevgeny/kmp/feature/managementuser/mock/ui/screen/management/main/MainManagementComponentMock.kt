package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.main

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.main.MainManagementComponent

/**
 * Mock implementation of [MainManagementComponent] for testing.
 */
@InternalApi
public open class MainManagementComponentMock : MainManagementComponent {
    public override fun onGlobalUserListClick(): Unit = Unit
    public override fun onGlobalSessionListClick(): Unit = Unit
    public override fun onGlobalIdentifierListClick(): Unit = Unit
    public override fun onAuditEventListClick(): Unit = Unit
    public override fun onSettingsClick(): Unit = Unit
    public override fun onGlobalStorageFileListClick(): Unit = Unit
}
