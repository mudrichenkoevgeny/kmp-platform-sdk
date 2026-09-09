package io.github.mudrichenkoevgeny.kmp.feature.auditapi.mock.ui.screen.detail

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.detail.AuditEventDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.detail.AuditEventDetailScreenState

@InternalApi
class AuditEventDetailComponentMock(
    initialState: AuditEventDetailScreenState = AuditEventDetailScreenState.Loading
) : AuditEventDetailComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<AuditEventDetailScreenState> = _state

    var backCalls = 0
    var retryCalls = 0

    fun updateState(state: AuditEventDetailScreenState) {
        _state.value = state
    }

    override fun onRetry() {
        retryCalls++
    }

    override fun onBackClick() {
        backCalls++
    }
}
