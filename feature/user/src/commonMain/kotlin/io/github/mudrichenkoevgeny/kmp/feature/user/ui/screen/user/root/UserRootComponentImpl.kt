package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.detail.IdentifierDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.SelfIdentifierListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.notifyIdentifierDeleted
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.detail.SessionDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SelfSessionListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.notifySessionRevoked
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.TotpMainComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.recovery.TotpRecoveryCodesComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main.MainUserComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
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
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.ScheduleUserDeletionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.DisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.EnableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.GetRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.RegenerateRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.SetupTotpUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientUserNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.identifier.detail.IdentifierDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.detail.SessionDetailParams

/**
 * Default [UserRootComponent] implementation: manages the user main and settings navigation stack.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param appType Application context ([AppType.CLIENT] or [AppType.MANAGEMENT]) to customize user flows.
 * @param userRepository Source of the current user state.
 * @param authStorage Encrypted token storage for session credentials.
 * @param identifierRepository Local repository tracking user identifiers.
 * @param logoutUseCase Ends the current session and clears local auth state.
 * @param scheduleUserDeletionUseCase Initiates account deletion for end-users.
 * @param getAvailableUserAuthProvidersUseCase Fetches supported authentication providers.
 * @param getUserIdentifiersUseCase Lists linked email and phone identifiers.
 * @param sendAddEmailIdentifierConfirmationUseCase Sends OTP confirmation to link a new email.
 * @param addUserIdentifierEmailUseCase Links a new email identifier using verified OTP.
 * @param sendAddPhoneIdentifierConfirmationUseCase Sends OTP confirmation to link a new phone number.
 * @param addUserIdentifierPhoneUseCase Links a new phone identifier using verified OTP.
 * @param addUserIdentifierGoogleUseCase Links a Google OAuth identity.
 * @param getSessionsUseCase Lists active authenticated sessions.
 * @param deleteSessionUseCase Terminates a specific session.
 * @param deleteAllOtherSessionsUseCase Terminates all sessions except the current active session.
 * @param setupTotpUseCase Generates TOTP secret and setup URI.
 * @param enableTotpUseCase Verifies code and enables TOTP 2FA.
 * @param disableTotpUseCase Disables TOTP 2FA for the user.
 * @param getRecoveryCodesUseCase Retrieves active backup recovery codes.
 * @param regenerateRecoveryCodesUseCase Generates a fresh set of backup recovery codes.
 * @param getSessionUseCase Optional use case to fetch single session details.
 * @param getUserIdentifierUseCase Optional use case to fetch single identifier details.
 * @param deleteUserIdentifierUseCase Optional use case to delete a linked identifier.
 * @param emailChangePasswordUseCase Optional use case to update account password.
 * @param onNavigateToLogin Invoked when the user logs out or requires re-authentication.
 */
class UserRootComponentImpl(
    componentContext: ComponentContext,
    private val appType: AppType,
    private val userRepository: UserRepository,
    private val authStorage: AuthStorage,
    private val identifierRepository: IdentifierRepository,
    private val logoutUseCase: LogoutUseCase,
    private val scheduleUserDeletionUseCase: ScheduleUserDeletionUseCase,
    private val getAvailableUserAuthProvidersUseCase: GetAvailableUserAuthProvidersUseCase,
    private val getUserIdentifiersUseCase: GetUserIdentifiersUseCase,
    private val sendAddEmailIdentifierConfirmationUseCase: SendAddEmailIdentifierConfirmationUseCase,
    private val addUserIdentifierEmailUseCase: AddUserIdentifierEmailUseCase,
    private val sendAddPhoneIdentifierConfirmationUseCase: SendAddPhoneIdentifierConfirmationUseCase,
    private val addUserIdentifierPhoneUseCase: AddUserIdentifierPhoneUseCase,
    private val addUserIdentifierGoogleUseCase: AddUserIdentifierGoogleUseCase,
    private val getSessionsUseCase: GetSessionsUseCase,
    private val deleteSessionUseCase: DeleteSessionUseCase,
    private val deleteAllOtherSessionsUseCase: DeleteAllOtherSessionsUseCase,
    private val setupTotpUseCase: SetupTotpUseCase,
    private val enableTotpUseCase: EnableTotpUseCase,
    private val disableTotpUseCase: DisableTotpUseCase,
    private val getRecoveryCodesUseCase: GetRecoveryCodesUseCase,
    private val regenerateRecoveryCodesUseCase: RegenerateRecoveryCodesUseCase,
    private val getSessionUseCase: GetSessionUseCase? = null,
    private val getUserIdentifierUseCase: GetUserIdentifierUseCase? = null,
    private val deleteUserIdentifierUseCase: DeleteUserIdentifierUseCase? = null,
    private val emailChangePasswordUseCase: EmailChangePasswordUseCase? = null,
    private val onNavigateToLogin: () -> Unit
) : UserRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<ClientUserNavigation>()

    override val stack: Value<ChildStack<ClientUserNavigation, UserRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = ClientUserNavigation.serializer(),
            initialConfiguration = ClientUserNavigation.Main,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override fun navigateTo(destination: ClientUserNavigation) {
        navigation.bringToFront(destination)
    }

    private fun handleBack(parentDestination: ClientUserNavigation? = ClientUserNavigation.Main) {
        if ((backHandler as? BackDispatcher)?.back() == true) {
            return
        }
        val items = stack.value.items
        val previousConfig = if (items.size >= 2) items[items.size - 2].configuration else null

        if (previousConfig != null) {
            navigation.pop()
        } else if (parentDestination != null && parentDestination != stack.value.active.configuration) {
            navigation.navigate { currentStack ->
                val index = currentStack.indexOfLast { it == parentDestination || it::class == parentDestination::class }
                if (index >= 0) {
                    currentStack.take(index + 1)
                } else {
                    currentStack.dropLast(1) + parentDestination
                }
            }
        }
    }

    private fun createChild(
        config: ClientUserNavigation,
        context: ComponentContext
    ): UserRootComponent.Child = when (config) {
        ClientUserNavigation.Main -> UserRootComponent.Child.Main(
            MainUserComponentImpl(
                componentContext = context,
                userRepository = userRepository,
                logoutUseCase = logoutUseCase,
                scheduleUserDeletionUseCase = scheduleUserDeletionUseCase,
                onNavigateToLogin = onNavigateToLogin,
                onNavigateToTotp = { navigation.bringToFront(ClientUserNavigation.TotpMain()) },
                onNavigateToSessions = { navigation.bringToFront(ClientUserNavigation.SessionList()) },
                onNavigateToIdentifiers = { navigation.bringToFront(ClientUserNavigation.IdentifierList()) }
            )
        )
        is ClientUserNavigation.TotpMain -> UserRootComponent.Child.TotpMain(
            TotpMainComponentImpl(
                componentContext = context,
                params = config.params,
                userRepository = userRepository,
                setupTotpUseCase = setupTotpUseCase,
                enableTotpUseCase = enableTotpUseCase,
                disableTotpUseCase = disableTotpUseCase,
                onNavigateToRecoveryCodes = { navigation.bringToFront(ClientUserNavigation.TotpRecoveryCodes) },
                onBack = { handleBack(ClientUserNavigation.Main) }
            )
        )
        ClientUserNavigation.TotpRecoveryCodes -> UserRootComponent.Child.TotpRecoveryCodes(
            TotpRecoveryCodesComponentImpl(
                componentContext = context,
                getRecoveryCodesUseCase = getRecoveryCodesUseCase,
                regenerateRecoveryCodesUseCase = regenerateRecoveryCodesUseCase,
                onBack = { handleBack(ClientUserNavigation.TotpMain()) }
            )
        )
        is ClientUserNavigation.SessionList -> UserRootComponent.Child.Sessions(
            SelfSessionListComponentImpl(
                componentContext = context,
                params = config.params,
                getSessionsUseCase = getSessionsUseCase,
                deleteSessionUseCase = deleteSessionUseCase,
                deleteAllOtherSessionsUseCase = deleteAllOtherSessionsUseCase,
                onNavigateToSessionDetail = { session ->
                    navigation.bringToFront(ClientUserNavigation.SessionDetail(SessionDetailParams(sessionId = session.id.asHexDashString())))
                },
                onBack = { handleBack(ClientUserNavigation.Main) },
                authStorage = authStorage
            )
        )
        is ClientUserNavigation.SessionDetail -> UserRootComponent.Child.SessionDetail(
            SessionDetailComponentImpl(
                componentContext = context,
                params = config.params,
                getSessionUseCase = getSessionUseCase,
                deleteSessionUseCase = deleteSessionUseCase,
                authStorage = authStorage,
                onSessionRevoked = { sessionId -> stack.value.notifySessionRevoked(sessionId) },
                onNavigateToIdentifierDetail = { identifierId ->
                    navigation.bringToFront(ClientUserNavigation.IdentifierDetail(IdentifierDetailParams(identifierId = identifierId.asHexDashString())))
                },
                onBack = { handleBack(ClientUserNavigation.SessionList()) }
            )
        )
        is ClientUserNavigation.IdentifierList -> UserRootComponent.Child.Identifiers(
            SelfIdentifierListComponentImpl(
                componentContext = context,
                params = config.params,
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
                    navigation.bringToFront(ClientUserNavigation.IdentifierDetail(IdentifierDetailParams(identifierId = identifierId.asHexDashString())))
                },
                onBack = { handleBack(ClientUserNavigation.Main) }
            )
        )
        is ClientUserNavigation.IdentifierDetail -> UserRootComponent.Child.IdentifierDetail(
            IdentifierDetailComponentImpl(
                componentContext = context,
                params = config.params,
                getUserIdentifierUseCase = getUserIdentifierUseCase,
                deleteUserIdentifierUseCase = deleteUserIdentifierUseCase,
                emailChangePasswordUseCase = emailChangePasswordUseCase,
                authStorage = authStorage,
                onIdentifierDeleted = { identifierId -> stack.value.notifyIdentifierDeleted(identifierId) },
                onBack = { handleBack(ClientUserNavigation.IdentifierList()) }
            )
        )
    }
}
