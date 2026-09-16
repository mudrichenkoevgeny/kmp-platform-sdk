package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.unlock.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.di.ClientUserComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.unlock.UnlockDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp.UnlockOtpComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection.UnlockMethodSelectionComponentImpl

/** Default implementation of [UnlockRootComponent]. */
class UnlockRootComponentImpl(
    componentContext: ComponentContext,
    private val clientUserComponent: ClientUserComponent,
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
                getUserIdentifiersUseCase = clientUserComponent.getUserIdentifiersUseCase,
                sendUnlockEmailConfirmationUseCase = clientUserComponent.sendUnlockEmailConfirmationUseCase,
                sendUnlockPhoneConfirmationUseCase = clientUserComponent.sendUnlockPhoneConfirmationUseCase,
                unlockByGoogleUseCase = clientUserComponent.unlockByGoogleUseCase,
                onNavigateToEmailOtp = { email ->
                    navigation.push(UnlockDestination.OtpInput(UnlockMethod.EMAIL, email))
                },
                onNavigateToPhoneOtp = { phone ->
                    navigation.push(UnlockDestination.OtpInput(UnlockMethod.PHONE, phone))
                },
                onUnlockSuccess = { navigation.push(UnlockDestination.Success) },
                onBack = { onFinished() }
            )
        )

        is UnlockDestination.OtpInput -> UnlockRootComponent.Child.OtpInput(
            UnlockOtpComponentImpl(
                componentContext = context,
                method = config.method,
                target = config.target,
                unlockByEmailUseCase = clientUserComponent.unlockByEmailUseCase,
                unlockByPhoneUseCase = clientUserComponent.unlockByPhoneUseCase,
                sendUnlockEmailConfirmationUseCase = clientUserComponent.sendUnlockEmailConfirmationUseCase,
                sendUnlockPhoneConfirmationUseCase = clientUserComponent.sendUnlockPhoneConfirmationUseCase,
                onUnlockSuccess = { navigation.push(UnlockDestination.Success) },
                onBack = { navigation.pop() }
            )
        )

        is UnlockDestination.Success -> UnlockRootComponent.Child.Success
    }

    override fun onDismiss() {
        onFinished()
    }
}
