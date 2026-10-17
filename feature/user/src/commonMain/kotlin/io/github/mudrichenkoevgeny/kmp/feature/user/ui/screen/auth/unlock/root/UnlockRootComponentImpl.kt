package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp.UnlockOtpComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection.UnlockMethodSelectionComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target.UnlockTargetInputComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockEmailConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockPhoneConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByGoogleUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientAuthNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.unlock.UnlockMethod
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.unlock.methodselection.UnlockMethodSelectionParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.unlock.otpinput.UnlockOtpInputParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.auth.unlock.targetinput.UnlockTargetInputParams

/**
 * Default implementation of [UnlockRootComponent].
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Account unlock parameters.
 * @param lockoutType Lockout cause.
 * @param lockoutUntil Temporary lockout expiration epoch timestamp in milliseconds.
 * @param getUserIdentifiersUseCase Fetches user identifiers for selection.
 * @param unlockByGoogleUseCase Triggers Google account unlock flow.
 * @param sendUnlockEmailConfirmationUseCase Sends unlock confirmation code to email.
 * @param sendUnlockPhoneConfirmationUseCase Sends unlock confirmation code to phone.
 * @param unlockByEmailUseCase Completes account unlock using email code.
 * @param unlockByPhoneUseCase Completes account unlock using phone code.
 * @param onFinished Invoked when account unlock flow completes.
 */
class UnlockRootComponentImpl(
    componentContext: ComponentContext,
    private val params: UnlockMethodSelectionParams? = null,
    private val lockoutType: AccountLockoutType? = params?.accountLockoutType,
    private val lockoutUntil: Long? = params?.temporaryLockoutUntil,
    private val getUserIdentifiersUseCase: GetUserIdentifiersUseCase? = null,
    private val unlockByGoogleUseCase: UnlockByGoogleUseCase? = null,
    private val sendUnlockEmailConfirmationUseCase: SendUnlockEmailConfirmationUseCase,
    private val sendUnlockPhoneConfirmationUseCase: SendUnlockPhoneConfirmationUseCase,
    private val unlockByEmailUseCase: UnlockByEmailUseCase,
    private val unlockByPhoneUseCase: UnlockByPhoneUseCase,
    private val onFinished: () -> Unit
) : UnlockRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<ClientAuthNavigation>()

    override val stack: Value<ChildStack<ClientAuthNavigation, UnlockRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = ClientAuthNavigation.serializer(),
            initialConfiguration = ClientAuthNavigation.UnlockMethodSelection(
                UnlockMethodSelectionParams(
                    accountLockoutType = lockoutType,
                    temporaryLockoutUntil = lockoutUntil
                )
            ),
            handleBackButton = true,
            childFactory = ::createChild
        )

    private fun handleBack(parentDestination: ClientAuthNavigation? = null) {
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
        } else {
            onFinished()
        }
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(
        config: ClientAuthNavigation,
        context: ComponentContext
    ): UnlockRootComponent.Child = when (config) {
        is ClientAuthNavigation.UnlockMethodSelection -> UnlockRootComponent.Child.MethodSelection(
            UnlockMethodSelectionComponentImpl(
                componentContext = context,
                params = config.params,
                lockoutType = lockoutType,
                lockoutUntil = lockoutUntil,
                getUserIdentifiersUseCase = getUserIdentifiersUseCase,
                unlockByGoogleUseCase = unlockByGoogleUseCase,
                onNavigateToEmailInput = {
                    navigation.bringToFront(
                        ClientAuthNavigation.UnlockTargetInput(
                            UnlockTargetInputParams(unlockMethod = UnlockMethod.EMAIL)
                        )
                    )
                },
                onNavigateToPhoneInput = {
                    navigation.bringToFront(
                        ClientAuthNavigation.UnlockTargetInput(
                            UnlockTargetInputParams(unlockMethod = UnlockMethod.PHONE)
                        )
                    )
                },
                onUnlockSuccess = { navigation.bringToFront(ClientAuthNavigation.UnlockSuccess()) },
                onBack = { onFinished() }
            )
        )

        is ClientAuthNavigation.UnlockTargetInput -> UnlockRootComponent.Child.TargetInput(
            UnlockTargetInputComponentImpl(
                componentContext = context,
                params = config.params,
                sendUnlockEmailConfirmationUseCase = sendUnlockEmailConfirmationUseCase,
                sendUnlockPhoneConfirmationUseCase = sendUnlockPhoneConfirmationUseCase,
                onNavigateToOtp = { target, initialDelaySeconds ->
                    navigation.bringToFront(
                        ClientAuthNavigation.UnlockOtpInput(
                            UnlockOtpInputParams(
                                unlockMethod = config.params.unlockMethod,
                                identifier = target,
                                retryAfterSeconds = initialDelaySeconds
                            )
                        )
                    )
                },
                onBack = { handleBack() }
            )
        )

        is ClientAuthNavigation.UnlockOtpInput -> UnlockRootComponent.Child.OtpInput(
            UnlockOtpComponentImpl(
                componentContext = context,
                params = config.params,
                unlockByEmailUseCase = unlockByEmailUseCase,
                unlockByPhoneUseCase = unlockByPhoneUseCase,
                sendUnlockEmailConfirmationUseCase = sendUnlockEmailConfirmationUseCase,
                sendUnlockPhoneConfirmationUseCase = sendUnlockPhoneConfirmationUseCase,
                onUnlockSuccess = { navigation.bringToFront(ClientAuthNavigation.UnlockSuccess()) },
                onBack = { handleBack() }
            )
        )
        is ClientAuthNavigation.UnlockSuccess,
        is ClientAuthNavigation.Auth,
        is ClientAuthNavigation.Main,
        is ClientAuthNavigation.LoginByEmail,
        is ClientAuthNavigation.LoginByPhone,
        is ClientAuthNavigation.LoginByTotp,
        is ClientAuthNavigation.Registration,
        is ClientAuthNavigation.ResetPassword,
        is ClientAuthNavigation.PendingDeletion -> UnlockRootComponent.Child.Success
    }

    override fun onDismiss() {
        onFinished()
    }
}
