package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import kotlinx.coroutines.launch
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.IdentifierListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.SessionListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.TotpSettingsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.DeleteUserIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.EmailChangePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.SendAddEmailIdentifierConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.SendAddPhoneIdentifierConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteAllOtherSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.LogoutUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.RestoreUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.ScheduleUserDeletionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.DisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.EnableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.GetRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.RegenerateRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.SetupTotpUseCase

/**
 * Default [ProfileRootComponent]: manages the profile navigation stack.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param appType Defines the application context (Client/Management) to toggle features like account deletion.
 * @param userRepository Source of the current user profile state.
 * @param logoutUseCase Ends the current session and clears local storage.
 * @param scheduleUserDeletionUseCase Initiates account deletion for end-users.
 * @param restoreUserUseCase Restores an account scheduled for deletion.
 * @param setupTotpUseCase Generates TOTP secret and setup URI.
 * @param enableTotpUseCase Verifies initial code and enables TOTP.
 * @param disableTotpUseCase Turns off TOTP for the account.
 * @param getRecoveryCodesUseCase Retrieves active backup recovery codes.
 * @param regenerateRecoveryCodesUseCase Generates a fresh set of backup codes.
 * @param getSessionsUseCase Lists active authenticated devices.
 * @param deleteSessionUseCase Terminates a specific remote session.
 * @param deleteAllOtherSessionsUseCase Terminates all other remote sessions.
 * @param getUserIdentifiersUseCase Lists linked emails and phone numbers.
 * @param deleteUserIdentifierUseCase Removes a linked identity record.
 * @param sendAddEmailIdentifierConfirmationUseCase Requests an OTP for a new email.
 * @param addUserIdentifierEmailUseCase Links a new email via verified OTP.
 * @param sendAddPhoneIdentifierConfirmationUseCase Requests an OTP for a new phone.
 * @param addUserIdentifierPhoneUseCase Links a new phone via verified OTP.
 * @param emailChangePasswordUseCase Updates account password.
 * @param onNavigateToLogin Invoked when the user needs to sign in (from the unauthorized state).
 */
class ProfileRootComponentImpl(
    componentContext: ComponentContext,
    private val appType: AppType,
    private val userRepository: UserRepository,
    private val logoutUseCase: LogoutUseCase,
    private val scheduleUserDeletionUseCase: ScheduleUserDeletionUseCase,
    private val restoreUserUseCase: RestoreUserUseCase,
    private val setupTotpUseCase: SetupTotpUseCase,
    private val enableTotpUseCase: EnableTotpUseCase,
    private val disableTotpUseCase: DisableTotpUseCase,
    private val getRecoveryCodesUseCase: GetRecoveryCodesUseCase,
    private val regenerateRecoveryCodesUseCase: RegenerateRecoveryCodesUseCase,
    private val getSessionsUseCase: GetSessionsUseCase,
    private val deleteSessionUseCase: DeleteSessionUseCase,
    private val deleteAllOtherSessionsUseCase: DeleteAllOtherSessionsUseCase,
    private val getUserIdentifiersUseCase: GetUserIdentifiersUseCase,
    private val deleteUserIdentifierUseCase: DeleteUserIdentifierUseCase,
    private val sendAddEmailIdentifierConfirmationUseCase: SendAddEmailIdentifierConfirmationUseCase,
    private val addUserIdentifierEmailUseCase: AddUserIdentifierEmailUseCase,
    private val sendAddPhoneIdentifierConfirmationUseCase: SendAddPhoneIdentifierConfirmationUseCase,
    private val addUserIdentifierPhoneUseCase: AddUserIdentifierPhoneUseCase,
    private val emailChangePasswordUseCase: EmailChangePasswordUseCase,
    private val onNavigateToLogin: () -> Unit
) : ProfileRootComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val navigation = StackNavigation<ProfileDestination>()

    init {
        scope.launch {
            userRepository.currentUser.collect { user ->
                if (user == null) {
                    navigation.popTo(0)
                }
            }
        }
    }

    override val stack: Value<ChildStack<ProfileDestination, ProfileRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = ProfileDestination.serializer(),
            initialConfiguration = ProfileDestination.Main,
            handleBackButton = true,
            childFactory = ::createChild
        )

    private fun createChild(
        config: ProfileDestination,
        context: ComponentContext
    ): ProfileRootComponent.Child = when (config) {
        is ProfileDestination.Main -> ProfileRootComponent.Child.Main(
            MainProfileComponentImpl(
                componentContext = context,
                appType = appType,
                userRepository = userRepository,
                logoutUseCase = logoutUseCase,
                scheduleUserDeletionUseCase = scheduleUserDeletionUseCase,
                restoreUserUseCase = restoreUserUseCase,
                onNavigateToLogin = onNavigateToLogin,
                onNavigateToTotp = { navigation.push(ProfileDestination.TotpSettings) },
                onNavigateToSessions = { navigation.push(ProfileDestination.Sessions) },
                onNavigateToIdentifiers = { navigation.push(ProfileDestination.Identifiers) }
            )
        )
        is ProfileDestination.TotpSettings -> ProfileRootComponent.Child.TotpSettings(
            TotpSettingsComponentImpl(
                componentContext = context,
                userRepository = userRepository,
                setupTotpUseCase = setupTotpUseCase,
                enableTotpUseCase = enableTotpUseCase,
                disableTotpUseCase = disableTotpUseCase,
                getRecoveryCodesUseCase = getRecoveryCodesUseCase,
                regenerateRecoveryCodesUseCase = regenerateRecoveryCodesUseCase,
                onBack = navigation::pop
            )
        )
        is ProfileDestination.Sessions -> ProfileRootComponent.Child.Sessions(
            SessionListComponentImpl(
                componentContext = context,
                getSessionsUseCase = getSessionsUseCase,
                deleteSessionUseCase = deleteSessionUseCase,
                deleteAllOtherSessionsUseCase = deleteAllOtherSessionsUseCase,
                onBack = navigation::pop
            )
        )
        is ProfileDestination.Identifiers -> ProfileRootComponent.Child.Identifiers(
            IdentifierListComponentImpl(
                componentContext = context,
                getUserIdentifiersUseCase = getUserIdentifiersUseCase,
                deleteUserIdentifierUseCase = deleteUserIdentifierUseCase,
                sendAddEmailIdentifierConfirmationUseCase = sendAddEmailIdentifierConfirmationUseCase,
                addUserIdentifierEmailUseCase = addUserIdentifierEmailUseCase,
                sendAddPhoneIdentifierConfirmationUseCase = sendAddPhoneIdentifierConfirmationUseCase,
                addUserIdentifierPhoneUseCase = addUserIdentifierPhoneUseCase,
                emailChangePasswordUseCase = emailChangePasswordUseCase,
                onBack = navigation::pop
            )
        )
    }
}
