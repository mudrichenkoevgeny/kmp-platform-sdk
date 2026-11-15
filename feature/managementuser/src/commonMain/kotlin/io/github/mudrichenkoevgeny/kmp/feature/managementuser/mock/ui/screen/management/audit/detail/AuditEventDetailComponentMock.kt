package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.audit.detail

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail.AuditEventDetailComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail.AuditEventDetailScreenState

/**
 * Mock implementation of [AuditEventDetailComponent] for testing.
 */
@InternalApi
public class AuditEventDetailComponentMock(
    initialState: AuditEventDetailScreenState = AuditEventDetailScreenState.Loading
) : AuditEventDetailComponent {

    private val _state = MutableValue(initialState)
    public override val state: Value<AuditEventDetailScreenState> = _state

    public var backCalls: Int = 0
    public var retryCalls: Int = 0
    public var resourceClickCalls: Int = 0
    public var subjectClickCalls: Int = 0

    /** Updates the current screen state. */
    public fun updateState(state: AuditEventDetailScreenState) {
        _state.value = state
    }

    public override fun onRetry() {
        retryCalls++
    }

    public override fun onBackClick() {
        backCalls++
    }

    public override fun onResourceClick() {
        resourceClickCalls++
    }

    public override fun onSubjectClick() {
        subjectClickCalls++
    }
}
