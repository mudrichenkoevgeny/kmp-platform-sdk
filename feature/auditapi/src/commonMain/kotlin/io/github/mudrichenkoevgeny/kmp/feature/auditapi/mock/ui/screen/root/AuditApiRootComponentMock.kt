package io.github.mudrichenkoevgeny.kmp.feature.auditapi.mock.ui.screen.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.root.AuditApiDestination
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.root.AuditApiRootComponent

@InternalApi
class AuditApiRootComponentMock(
    initialChild: AuditApiRootComponent.Child,
    initialConfiguration: AuditApiDestination = AuditApiDestination.Main
) : AuditApiRootComponent {

    private val _stack = MutableValue(
        ChildStack(
            configuration = initialConfiguration,
            instance = initialChild
        )
    )

    override val stack: Value<ChildStack<AuditApiDestination, AuditApiRootComponent.Child>> = _stack

    var backCalls = 0

    fun setChild(configuration: AuditApiDestination, child: AuditApiRootComponent.Child) {
        _stack.value = ChildStack(
            configuration = configuration,
            instance = child
        )
    }

    override fun onBackClick() {
        backCalls++
    }
}
