package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.auth

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.auth.settings.ManagementAuthSettingsRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.settings.SettingsDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.GetManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.ResetRemoteAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.SaveRemoteAuthSettingsUseCase
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNull

@InternalApi
class EditAuthSettingsComponentImplTest {

    @Test
    fun onResetClick_activatesConfirmResetDialogSlot() = runComponentTest {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val repository = ManagementAuthSettingsRepositoryMock()
        val component = EditAuthSettingsComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            initialDialogConfig = null,
            getManagementAuthSettingsUseCase = GetManagementAuthSettingsUseCase(repository),
            saveRemoteAuthSettingsUseCase = SaveRemoteAuthSettingsUseCase(repository),
            resetRemoteAuthSettingsUseCase = ResetRemoteAuthSettingsUseCase(repository),
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
