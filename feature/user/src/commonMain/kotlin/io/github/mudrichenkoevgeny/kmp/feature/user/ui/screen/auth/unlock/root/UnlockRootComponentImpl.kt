package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.UnlockDestination
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

/** Default implementation of [UnlockRootComponent]. */
class UnlockRootComponentImpl(
    componentContext: ComponentContext,
    private val lockoutType: AccountLockoutType? = null,
    private val lockoutUntil: Long? = null,
    private val getUserIdentifiersUseCase: GetUserIdentifiersUseCase? = null,
    private val unlockByGoogleUseCase: UnlockByGoogleUseCase? = null,
    private val sendUnlockEmailConfirmationUseCase: SendUnlockEmailConfirmationUseCase,
    private val sendUnlockPhoneConfirmationUseCase: SendUnlockPhoneConfirmationUseCase,
    private val unlockByEmailUseCase: UnlockByEmailUseCase,
    private val unlockByPhoneUseCase: UnlockByPhoneUseCase,
    private val onFinished: () -> Unit
) : UnlockRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<UnlockDestination>()

    override val stack: Value<ChildStack<UnlockDestination, UnlockRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = UnlockDestination.serializer(),
            initialConfiguration = UnlockDestination.MethodSelection,
            handleBackButton = true,
            childFactory = ::createChild
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(
        config: UnlockDestination,
        context: ComponentContext
    ): UnlockRootComponent.Child = when (config) {
        is UnlockDestination.MethodSelection -> UnlockRootComponent.Child.MethodSelection(
            UnlockMethodSelectionComponentImpl(
                componentContext = context,
                lockoutType = lockoutType,
                lockoutUntil = lockoutUntil,
                getUserIdentifiersUseCase = getUserIdentifiersUseCase,
                unlockByGoogleUseCase = unlockByGoogleUseCase,
                onNavigateToEmailInput = {
                    navigation.bringToFront(UnlockDestination.TargetInput(UnlockMethod.EMAIL))
                },
                onNavigateToPhoneInput = {
                    navigation.bringToFront(UnlockDestination.TargetInput(UnlockMethod.PHONE))
                },
                onUnlockSuccess = { navigation.bringToFront(UnlockDestination.Success) },
                onBack = { onFinished() }
            )
        )

        is UnlockDestination.TargetInput -> UnlockRootComponent.Child.TargetInput(
            UnlockTargetInputComponentImpl(
                componentContext = context,
                method = config.method,
                sendUnlockEmailConfirmationUseCase = sendUnlockEmailConfirmationUseCase,
                sendUnlockPhoneConfirmationUseCase = sendUnlockPhoneConfirmationUseCase,
                onNavigateToOtp = { target, initialDelaySeconds ->
                    navigation.bringToFront(UnlockDestination.OtpInput(config.method, target, initialDelaySeconds))
                },
                onBack = { navigation.pop() }
            )
        )

        is UnlockDestination.OtpInput -> UnlockRootComponent.Child.OtpInput(
            UnlockOtpComponentImpl(
                componentContext = context,
                method = config.method,
                target = config.target,
                initialDelaySeconds = config.initialDelaySeconds,
                unlockByEmailUseCase = unlockByEmailUseCase,
                unlockByPhoneUseCase = unlockByPhoneUseCase,
                sendUnlockEmailConfirmationUseCase = sendUnlockEmailConfirmationUseCase,
                sendUnlockPhoneConfirmationUseCase = sendUnlockPhoneConfirmationUseCase,
                onUnlockSuccess = { navigation.bringToFront(UnlockDestination.Success) },
                onBack = { navigation.pop() }
            )
        )

        is UnlockDestination.Success -> UnlockRootComponent.Child.Success
    }

    override fun onDismiss() {
        onFinished()
    }
}
