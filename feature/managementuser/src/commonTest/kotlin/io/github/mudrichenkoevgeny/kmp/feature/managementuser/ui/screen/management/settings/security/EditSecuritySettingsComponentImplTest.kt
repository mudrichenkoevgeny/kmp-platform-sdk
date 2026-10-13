package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.security

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.security.settings.ManagementSecuritySettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.GetManagementSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.ResetRemoteSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.SaveRemoteSecuritySettingsUseCase
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNull

@InternalApi
class EditSecuritySettingsComponentImplTest {

    @Test
    fun onResetClick_activatesConfirmResetDialogSlot() = runComponentTest {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val repository = ManagementSecuritySettingsRepositoryMock()
        val component = EditSecuritySettingsComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            initialDialogConfig = null,
            getManagementSecuritySettingsUseCase = GetManagementSecuritySettingsUseCase(repository),
            saveRemoteSecuritySettingsUseCase = SaveRemoteSecuritySettingsUseCase(repository),
            resetRemoteSecuritySettingsUseCase = ResetRemoteSecuritySettingsUseCase(repository),
            onBack = {}
        )

        try {
            assertNull(component.dialogSlot.value.child)
            component.onResetClick()
            assertIs<SettingsDialogChild.ConfirmReset>(component.dialogSlot.value.child?.instance)
            component.onDismissDialog()
            assertNull(component.dialogSlot.value.child)
        } finally {
            lifecycle.destroy()
        }
    }
}
