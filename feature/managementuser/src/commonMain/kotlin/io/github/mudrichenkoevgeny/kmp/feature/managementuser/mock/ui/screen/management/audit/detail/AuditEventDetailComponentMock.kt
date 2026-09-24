package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.audit.detail

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail.AuditEventDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail.AuditEventDetailScreenState

@InternalApi
class AuditEventDetailComponentMock(
    initialState: AuditEventDetailScreenState = AuditEventDetailScreenState.Loading
) : AuditEventDetailComponent {

    private val _state = MutableValue(initialState)
    override val state: Value<AuditEventDetailScreenState> = _state

    var backCalls = 0
    var retryCalls = 0
    var resourceClickCalls = 0
    var subjectClickCalls = 0

    fun updateState(state: AuditEventDetailScreenState) {
        _state.value = state
    }

    override fun onRetry() {
        retryCalls++
    }

    override fun onBackClick() {
        backCalls++
    }

    override fun onResourceClick() {
        resourceClickCalls++
    }

    override fun onSubjectClick() {
        subjectClickCalls++
    }
}