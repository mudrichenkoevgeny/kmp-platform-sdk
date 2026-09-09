package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.usecase.GetAuditEventUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import kotlinx.coroutines.launch

/**
 * Default implementation of [AuditEventDetailComponent].
 *
 * @param componentContext Decompose [ComponentContext].
 * @param eventId Unique audit event identifier.
 * @param getAuditEventUseCase Fetches audit event details.
 * @param onBack Pops this screen from the navigation stack.
 */
class AuditEventDetailComponentImpl(
    componentContext: ComponentContext,
    private val eventId: AuditEventId,
    private val getAuditEventUseCase: GetAuditEventUseCase,
    private val onBack: () -> Unit
) : AuditEventDetailComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<AuditEventDetailScreenState>(AuditEventDetailScreenState.Loading)
    override val state: Value<AuditEventDetailScreenState> = _state

    init {
        loadEvent()
    }

    override fun onRetry() {
        loadEvent()
    }

    override fun onBackClick() {
        onBack()
    }

    private fun loadEvent() {
        _state.value = AuditEventDetailScreenState.Loading
        scope.launch {
            getAuditEventUseCase(eventId.value.toString())
                .onSuccess { event ->
                    _state.value = AuditEventDetailScreenState.Content(event = event)
                }
                .onError { error ->
                    _state.value = AuditEventDetailScreenState.Error(error)
                }
        }
    }
}
