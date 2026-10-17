package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit.GetAuditEventUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.toAuditEventIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.screen.management.audit.detail.ManagementAuditEventDetailParams
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.resource.UserAuditResourceType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.toUserIdentifierIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.toUserIdOrNull
import kotlinx.coroutines.launch

/**
 * Default implementation of [AuditEventDetailComponent].
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Audit event detail parameters.
 * @param userRepository Source of the current user profile state.
 * @param getAuditEventUseCase Fetches audit event details.
 * @param onNavigateToUserDetail Navigates to user detail screen.
 * @param onNavigateToSessionDetail Navigates to session detail screen.
 * @param onNavigateToIdentifierDetail Navigates to identifier detail screen.
 * @param onNavigateToProfile Navigates to main profile screen.
 * @param onBack Pops this screen from the navigation stack.
 */
class AuditEventDetailComponentImpl(
    componentContext: ComponentContext,
    private val params: ManagementAuditEventDetailParams,
    private val userRepository: UserRepository,
    private val getAuditEventUseCase: GetAuditEventUseCase,
    private val onNavigateToUserDetail: ((UserId) -> Unit)? = null,
    private val onNavigateToSessionDetail: ((UserSessionId) -> Unit)? = null,
    private val onNavigateToIdentifierDetail: ((UserIdentifierId) -> Unit)? = null,
    private val onNavigateToProfile: () -> Unit = {},
    private val onBack: () -> Unit
) : AuditEventDetailComponent, ComponentContext by componentContext {

    private val eventId: AuditEventId? get() = params.eventId.toAuditEventIdOrNull()

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<AuditEventDetailScreenState>(AuditEventDetailScreenState.Loading)
    override val state: Value<AuditEventDetailScreenState> = _state

    private var currentUserId: UserId? = null

    init {
        loadCurrentUserAndEvent()
    }

    override fun onRetry() {
        loadEventDetails()
    }

    override fun onSubjectClick() {
        val currentContent = _state.value as? AuditEventDetailScreenState.Content ?: return
        val actorUserId = currentContent.event.actorId?.toUserIdOrNull() ?: return

        if (onNavigateToUserDetail != null) {
            onNavigateToUserDetail.invoke(actorUserId)
        } else {
            onNavigateToProfile()
        }
    }

    override fun onResourceClick() {
        val currentContent = _state.value as? AuditEventDetailScreenState.Content ?: return
        val event = currentContent.event
        val resourceType = event.resource
        val resourceId = event.resourceId ?: return

        when (resourceType) {
            UserAuditResourceType.USER -> {
                val targetUserId = resourceId.toUserIdOrNull() ?: return
                if (onNavigateToUserDetail != null) {
                    onNavigateToUserDetail.invoke(targetUserId)
                } else {
                    onNavigateToProfile()
                }
            }
            UserAuditResourceType.SESSION -> {
                val targetSessionId = resourceId.toUserSessionIdOrNull() ?: return
                onNavigateToSessionDetail?.invoke(targetSessionId)
            }
            UserAuditResourceType.IDENTIFIER -> {
                val targetIdentifierId = resourceId.toUserIdentifierIdOrNull() ?: return
                onNavigateToIdentifierDetail?.invoke(targetIdentifierId)
            }
            else -> {}
        }
    }

    override fun onBackClick() {
        onBack()
    }

    private fun loadCurrentUserAndEvent() {
        scope.launch {
            userRepository.currentUser.collect { user ->
                currentUserId = user?.id
            }
        }
        loadEventDetails()
    }

    private fun loadEventDetails() {
        val targetEventId = eventId
        if (targetEventId == null) {
            _state.value = AuditEventDetailScreenState.Error(
                CommonError.ContractViolation(throwable = IllegalStateException("Invalid eventId in params"))
            )
            return
        }

        _state.value = AuditEventDetailScreenState.Loading

        scope.launch {
            getAuditEventUseCase(targetEventId.asHexDashString())
                .onSuccess { event ->
                    _state.value = AuditEventDetailScreenState.Content(event = event)
                }
                .onError { error ->
                    _state.value = AuditEventDetailScreenState.Error(error)
                }
        }
    }
}
