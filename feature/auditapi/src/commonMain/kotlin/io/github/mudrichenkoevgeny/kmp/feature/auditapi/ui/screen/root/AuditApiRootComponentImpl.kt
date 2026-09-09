package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.detail.AuditEventDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.events.AuditEventsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.usecase.GetAuditEventUseCase
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.usecase.GetAuditEventsUseCase
import com.arkivanov.decompose.DelicateDecomposeApi

/**
 * Default implementation of [AuditApiRootComponent].
 *
 * @param componentContext Decompose [ComponentContext].
 * @param getAuditEventsUseCase Fetches paginated audit events.
 * @param getAuditEventUseCase Fetches audit event details.
 * @param onBack Navigation back callback.
 */
@OptIn(DelicateDecomposeApi::class)
class AuditApiRootComponentImpl(
    componentContext: ComponentContext,
    private val getAuditEventsUseCase: GetAuditEventsUseCase,
    private val getAuditEventUseCase: GetAuditEventUseCase,
    private val onBack: () -> Unit
) : AuditApiRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<AuditApiDestination>()

    override val stack: Value<ChildStack<AuditApiDestination, AuditApiRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = AuditApiDestination.serializer(),
            initialConfiguration = AuditApiDestination.Main,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override fun onBackClick() {
        onBack()
    }

    private fun createChild(
        config: AuditApiDestination,
        context: ComponentContext
    ): AuditApiRootComponent.Child = when (config) {
        is AuditApiDestination.Main -> AuditApiRootComponent.Child.Main(
            AuditEventsComponentImpl(
                componentContext = context,
                getAuditEventsUseCase = getAuditEventsUseCase,
                onNavigateToEventDetail = { eventId -> navigation.push(AuditApiDestination.Detail(eventId.value.toString())) },
                onBack = onBack
            )
        )
        is AuditApiDestination.Detail -> AuditApiRootComponent.Child.Detail(
            AuditEventDetailComponentImpl(
                componentContext = context,
                eventId = config.eventId,
                getAuditEventUseCase = getAuditEventUseCase,
                onBack = navigation::pop
            )
        )
    }
}
