package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.detail.IdentifierDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.SelfIdentifierListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list.notifyIdentifierDeleted
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail.SessionDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list.SelfSessionListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.list.notifySessionRevoked
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.recovery.TotpRecoveryCodesComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.ObserveAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierGoogleUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.DeleteUserIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.EmailChangePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.SendAddEmailIdentifierConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.SendAddPhoneIdentifierConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteAllOtherSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.LogoutUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.GetUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.RestoreUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.ScheduleUserDeletionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.DisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.EnableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.GetRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.RegenerateRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.SetupTotpUseCase
import kotlinx.coroutines.launch

/**
 * Default [ProfileRootComponent]: manages the profile navigation stack.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param appType Defines the application context (Client/Management) to toggle features like account deletion.
 * @param userRepository Source of the current user profile state.
 * @param logoutUseCase Ends the current session and clears local storage.
 * @param scheduleUserDeletionUseCase Initiates account deletion for end-users.
 * @param restoreUserUseCase Restores an account scheduled for deletion.
 * @param getAuthSettingsUseCase Retrieves current remote auth settings.
 * @param observeAuthSettingsUseCase Observes current remote auth settings updates in real time.
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
    private val restoreUserUseCase: RestoreUserUseCase? = null,
    private val getUserUseCase: GetUserUseCase? = null,
    private val getAuthSettingsUseCase: GetAuthSettingsUseCase? = null,
    private val observeAuthSettingsUseCase: ObserveAuthSettingsUseCase? = null,
    private val setupTotpUseCase: SetupTotpUseCase,
    private val enableTotpUseCase: EnableTotpUseCase,
    private val disableTotpUseCase: DisableTotpUseCase,
    private val getRecoveryCodesUseCase: GetRecoveryCodesUseCase,
    private val regenerateRecoveryCodesUseCase: RegenerateRecoveryCodesUseCase,
    private val getSessionsUseCase: GetSessionsUseCase,
    private val getSessionUseCase: GetSessionUseCase? = null,
    private val getAvailableUserAuthProvidersUseCase: GetAvailableUserAuthProvidersUseCase? = null,
    private val deleteSessionUseCase: DeleteSessionUseCase,
    private val deleteAllOtherSessionsUseCase: DeleteAllOtherSessionsUseCase,
    private val getUserIdentifiersUseCase: GetUserIdentifiersUseCase,
    private val getUserIdentifierUseCase: GetUserIdentifierUseCase? = null,
    private val deleteUserIdentifierUseCase: DeleteUserIdentifierUseCase? = null,
    private val identifierRepository: IdentifierRepository? = null,
    private val sendAddEmailIdentifierConfirmationUseCase: SendAddEmailIdentifierConfirmationUseCase? = null,
    private val addUserIdentifierEmailUseCase: AddUserIdentifierEmailUseCase? = null,
    private val authStorage: AuthStorage? = null,
    private val sendAddPhoneIdentifierConfirmationUseCase: SendAddPhoneIdentifierConfirmationUseCase? = null,
    private val addUserIdentifierPhoneUseCase: AddUserIdentifierPhoneUseCase? = null,
    private val addUserIdentifierGoogleUseCase: AddUserIdentifierGoogleUseCase? = null,
    private val emailChangePasswordUseCase: EmailChangePasswordUseCase? = null,
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
        ProfileDestination.Main -> ProfileRootComponent.Child.Main(
            MainProfileComponentImpl(
                componentContext = context,
                appType = appType,
                userRepository = userRepository,
                logoutUseCase = logoutUseCase,
                scheduleUserDeletionUseCase = scheduleUserDeletionUseCase,
                getUserUseCase = getUserUseCase,
                getAuthSettingsUseCase = getAuthSettingsUseCase,
                observeAuthSettingsUseCase = observeAuthSettingsUseCase,
                onNavigateToLogin = onNavigateToLogin,
                onNavigateToTotp = { navigation.bringToFront(ProfileDestination.TotpMain) },
                onNavigateToSessions = { navigation.bringToFront(ProfileDestination.Sessions) },
                onNavigateToIdentifiers = { navigation.bringToFront(ProfileDestination.Identifiers) }
            )
        )
        ProfileDestination.TotpMain -> ProfileRootComponent.Child.TotpMain(
            TotpMainComponentImpl(
                componentContext = context,
                userRepository = userRepository,
                setupTotpUseCase = setupTotpUseCase,
                enableTotpUseCase = enableTotpUseCase,
                disableTotpUseCase = disableTotpUseCase,
                onNavigateToRecoveryCodes = { navigation.bringToFront(ProfileDestination.TotpRecoveryCodes) },
                onBack = navigation::pop
            )
        )
        ProfileDestination.TotpRecoveryCodes -> ProfileRootComponent.Child.TotpRecoveryCodes(
            TotpRecoveryCodesComponentImpl(
                componentContext = context,
                getRecoveryCodesUseCase = getRecoveryCodesUseCase,
                regenerateRecoveryCodesUseCase = regenerateRecoveryCodesUseCase,
                onBack = navigation::pop
            )
        )
        ProfileDestination.Sessions -> ProfileRootComponent.Child.Sessions(
            SelfSessionListComponentImpl(
                componentContext = context,
                getSessionsUseCase = getSessionsUseCase,
                deleteSessionUseCase = deleteSessionUseCase,
                deleteAllOtherSessionsUseCase = deleteAllOtherSessionsUseCase,
                onNavigateToSessionDetail = { session ->
                    navigation.bringToFront(ProfileDestination.SessionDetail(session.id.asHexDashString()))
                },
                onBack = navigation::pop,
                authStorage = authStorage
            )
        )
        is ProfileDestination.SessionDetail -> ProfileRootComponent.Child.SessionDetail(
            SessionDetailComponentImpl(
                componentContext = context,
                sessionId = config.sessionId,
                getSessionUseCase = getSessionUseCase,
                deleteSessionUseCase = deleteSessionUseCase,
                authStorage = authStorage,
                onSessionRevoked = { stack.value.notifySessionRevoked(it) },
                onNavigateToIdentifierDetail = { identifierId ->
                    navigation.bringToFront(ProfileDestination.IdentifierDetail(identifierId.asHexDashString()))
                },
                onBack = navigation::pop
            )
        )
        ProfileDestination.Identifiers -> ProfileRootComponent.Child.Identifiers(
            SelfIdentifierListComponentImpl(
                componentContext = context,
                appType = appType,
                getUserIdentifiersUseCase = getUserIdentifiersUseCase,
                getAvailableUserAuthProvidersUseCase = getAvailableUserAuthProvidersUseCase,
                sendAddEmailIdentifierConfirmationUseCase = sendAddEmailIdentifierConfirmationUseCase,
                addUserIdentifierEmailUseCase = addUserIdentifierEmailUseCase,
                sendAddPhoneIdentifierConfirmationUseCase = sendAddPhoneIdentifierConfirmationUseCase,
                addUserIdentifierPhoneUseCase = addUserIdentifierPhoneUseCase,
                addUserIdentifierGoogleUseCase = addUserIdentifierGoogleUseCase,
                identifierRepository = identifierRepository,
                authStorage = authStorage,
                onIdentifierSelect = { identifierId ->
                    navigation.bringToFront(ProfileDestination.IdentifierDetail(identifierId.asHexDashString()))
                },
                onBack = navigation::pop
            )
        )
        is ProfileDestination.IdentifierDetail -> ProfileRootComponent.Child.IdentifierDetail(
            IdentifierDetailComponentImpl(
                componentContext = context,
                identifierId = config.identifierId,
                getUserIdentifierUseCase = getUserIdentifierUseCase,
                deleteUserIdentifierUseCase = deleteUserIdentifierUseCase,
                emailChangePasswordUseCase = emailChangePasswordUseCase,
                authStorage = authStorage,
                onIdentifierDeleted = { stack.value.notifyIdentifierDeleted(it) },
                onBack = navigation::pop
            )
        )
    }
}
