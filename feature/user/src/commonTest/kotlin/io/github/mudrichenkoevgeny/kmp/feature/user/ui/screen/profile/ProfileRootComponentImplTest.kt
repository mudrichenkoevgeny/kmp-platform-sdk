package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.UserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.AddUserIdentifierEmailUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.AddUserIdentifierPhoneUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.DeleteUserIdentifierUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.EmailChangePasswordUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.GetUserIdentifiersUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.SendAddEmailIdentifierConfirmationUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.identifier.SendAddPhoneIdentifierConfirmationUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.DeleteAllOtherSessionsUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.DeleteSessionUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.GetSessionsUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.LogoutUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.RestoreUserUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.ScheduleUserDeletionUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security.DisableTotpUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security.EnableTotpUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security.GetRecoveryCodesUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security.RegenerateRecoveryCodesUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security.SetupTotpUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.IdentifierListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.SessionListComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.TotpSettingsComponentImpl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class ProfileRootComponentImplTest {

    @Test
    fun initialStack_startsAtMain() = runComponentTest {
        val context = createProfileRootComponentTestContext()
        try {
            val currentChild = context.component.stack.value.active.instance
            assertIs<ProfileRootComponent.Child.Main>(currentChild)
            assertEquals(ProfileDestination.Main, context.component.stack.value.active.configuration)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onNavigateToLogin_invokesRootCallback() = runComponentTest {
        val context = createProfileRootComponentTestContext()
        try {
            val mainChild = assertIs<ProfileRootComponent.Child.Main>(context.component.stack.value.active.instance)
            val mainComponent = assertIs<MainProfileComponentImpl>(mainChild.component)

            mainComponent.onLoginClick()

            assertEquals(ONE_CALL, context.onNavigateToLoginCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun navigateToTotp_pushesTotpSettingsAndPopsBack() = runComponentTest {
        val context = createProfileRootComponentTestContext()
        try {
            val mainChild = assertIs<ProfileRootComponent.Child.Main>(context.component.stack.value.active.instance)
            val mainComponent = assertIs<MainProfileComponentImpl>(mainChild.component)

            mainComponent.onTotpSettingsClick()

            val totpChild = assertIs<ProfileRootComponent.Child.TotpSettings>(context.component.stack.value.active.instance)
            assertEquals(ProfileDestination.TotpSettings, context.component.stack.value.active.configuration)

            val totpComponent = assertIs<TotpSettingsComponentImpl>(totpChild.component)
            totpComponent.onBack()

            assertIs<ProfileRootComponent.Child.Main>(context.component.stack.value.active.instance)
            assertEquals(ProfileDestination.Main, context.component.stack.value.active.configuration)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun navigateToSessions_pushesSessionsAndPopsBack() = runComponentTest {
        val context = createProfileRootComponentTestContext()
        try {
            val mainChild = assertIs<ProfileRootComponent.Child.Main>(context.component.stack.value.active.instance)
            val mainComponent = assertIs<MainProfileComponentImpl>(mainChild.component)

            mainComponent.onSessionsClick()

            val sessionsChild = assertIs<ProfileRootComponent.Child.Sessions>(context.component.stack.value.active.instance)
            assertEquals(ProfileDestination.Sessions, context.component.stack.value.active.configuration)

            val sessionsComponent = assertIs<SessionListComponent>(sessionsChild.component)
            sessionsComponent.onBackClick()

            assertIs<ProfileRootComponent.Child.Main>(context.component.stack.value.active.instance)
            assertEquals(ProfileDestination.Main, context.component.stack.value.active.configuration)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun navigateToIdentifiers_pushesIdentifiersAndPopsBack() = runComponentTest {
        val context = createProfileRootComponentTestContext()
        try {
            val mainChild = assertIs<ProfileRootComponent.Child.Main>(context.component.stack.value.active.instance)
            val mainComponent = assertIs<MainProfileComponentImpl>(mainChild.component)

            mainComponent.onIdentifiersClick()

            val identifiersChild = assertIs<ProfileRootComponent.Child.Identifiers>(context.component.stack.value.active.instance)
            assertEquals(ProfileDestination.Identifiers, context.component.stack.value.active.configuration)

            val identifiersComponent = assertIs<IdentifierListComponent>(identifiersChild.component)
            identifiersComponent.onBackClick()

            assertIs<ProfileRootComponent.Child.Main>(context.component.stack.value.active.instance)
            assertEquals(ProfileDestination.Main, context.component.stack.value.active.configuration)
        } finally {
            context.destroy()
        }
    }

    private fun createProfileRootComponentTestContext(
        appType: AppType = AppType.CLIENT,
        userRepository: UserRepositoryMock = UserRepositoryMock(),
        logoutUseCase: LogoutUseCaseMock = LogoutUseCaseMock(),
        scheduleUserDeletionUseCase: ScheduleUserDeletionUseCaseMock = ScheduleUserDeletionUseCaseMock(),
        restoreUserUseCase: RestoreUserUseCaseMock = RestoreUserUseCaseMock(),
        setupTotpUseCase: SetupTotpUseCaseMock = SetupTotpUseCaseMock(),
        enableTotpUseCase: EnableTotpUseCaseMock = EnableTotpUseCaseMock(),
        disableTotpUseCase: DisableTotpUseCaseMock = DisableTotpUseCaseMock(),
        getRecoveryCodesUseCase: GetRecoveryCodesUseCaseMock = GetRecoveryCodesUseCaseMock(),
        regenerateRecoveryCodesUseCase: RegenerateRecoveryCodesUseCaseMock = RegenerateRecoveryCodesUseCaseMock(),
        getSessionsUseCase: GetSessionsUseCaseMock = GetSessionsUseCaseMock(),
        deleteSessionUseCase: DeleteSessionUseCaseMock = DeleteSessionUseCaseMock(),
        deleteAllOtherSessionsUseCase: DeleteAllOtherSessionsUseCaseMock = DeleteAllOtherSessionsUseCaseMock(),
        getUserIdentifiersUseCase: GetUserIdentifiersUseCaseMock = GetUserIdentifiersUseCaseMock(),
        deleteUserIdentifierUseCase: DeleteUserIdentifierUseCaseMock = DeleteUserIdentifierUseCaseMock(),
        sendAddEmailIdentifierConfirmationUseCase: SendAddEmailIdentifierConfirmationUseCaseMock = SendAddEmailIdentifierConfirmationUseCaseMock(),
        addUserIdentifierEmailUseCase: AddUserIdentifierEmailUseCaseMock = AddUserIdentifierEmailUseCaseMock(),
        sendAddPhoneIdentifierConfirmationUseCase: SendAddPhoneIdentifierConfirmationUseCaseMock = SendAddPhoneIdentifierConfirmationUseCaseMock(),
        addUserIdentifierPhoneUseCase: AddUserIdentifierPhoneUseCaseMock = AddUserIdentifierPhoneUseCaseMock(),
        emailChangePasswordUseCase: EmailChangePasswordUseCaseMock = EmailChangePasswordUseCaseMock()
    ): ProfileRootComponentTestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = ProfileRootComponentTestContext(lifecycle = lifecycle)

        context.component = ProfileRootComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            appType = appType,
            userRepository = userRepository,
            logoutUseCase = logoutUseCase,
            scheduleUserDeletionUseCase = scheduleUserDeletionUseCase,
            restoreUserUseCase = restoreUserUseCase,
            setupTotpUseCase = setupTotpUseCase,
            enableTotpUseCase = enableTotpUseCase,
            disableTotpUseCase = disableTotpUseCase,
            getRecoveryCodesUseCase = getRecoveryCodesUseCase,
            regenerateRecoveryCodesUseCase = regenerateRecoveryCodesUseCase,
            getSessionsUseCase = getSessionsUseCase,
            deleteSessionUseCase = deleteSessionUseCase,
            deleteAllOtherSessionsUseCase = deleteAllOtherSessionsUseCase,
            getUserIdentifiersUseCase = getUserIdentifiersUseCase,
            deleteUserIdentifierUseCase = deleteUserIdentifierUseCase,
            sendAddEmailIdentifierConfirmationUseCase = sendAddEmailIdentifierConfirmationUseCase,
            addUserIdentifierEmailUseCase = addUserIdentifierEmailUseCase,
            sendAddPhoneIdentifierConfirmationUseCase = sendAddPhoneIdentifierConfirmationUseCase,
            addUserIdentifierPhoneUseCase = addUserIdentifierPhoneUseCase,
            emailChangePasswordUseCase = emailChangePasswordUseCase,
            onNavigateToLogin = { context.onNavigateToLoginCalls++ }
        )

        return context
    }

    private class ProfileRootComponentTestContext(
        val lifecycle: LifecycleRegistry
    ) {
        lateinit var component: ProfileRootComponentImpl
        var onNavigateToLoginCalls: Int = 0

        fun destroy() = lifecycle.destroy()
    }

    private companion object {
        const val ONE_CALL = 1
    }
}