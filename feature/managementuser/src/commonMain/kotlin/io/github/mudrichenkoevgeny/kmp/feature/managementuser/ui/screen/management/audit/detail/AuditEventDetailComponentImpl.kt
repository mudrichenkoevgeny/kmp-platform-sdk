package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit.GetAuditEventUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
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
 * @param eventId Unique audit event identifier.
 * @param getAuditEventUseCase Fetches audit event details.
 * @param currentUserId Active authenticated user ID for self checks.
 * @param onNavigateToUserDetail Navigates to user detail screen.
 * @param onNavigateToSessionDetail Navigates to session detail screen.
 * @param onNavigateToIdentifierDetail Navigates to identifier detail screen.
 * @param onNavigateToProfile Navigates to main profile screen.
 * @param onBack Pops this screen from the navigation stack.
 */
class AuditEventDetailComponentImpl(
    componentContext: ComponentContext,
    private val eventId: AuditEventId,
    private val getAuditEventUseCase: GetAuditEventUseCase,
    private val currentUserId: UserId? = null,
    private val onNavigateToUserDetail: ((UserId) -> Unit)? = null,
    private val onNavigateToSessionDetail: ((UserSessionId) -> Unit)? = null,
    private val onNavigateToIdentifierDetail: ((UserIdentifierId) -> Unit)? = null,
    private val onNavigateToProfile: (() -> Unit)? = null,
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

    override fun onResourceClick() {
        val event = (_state.value as? AuditEventDetailScreenState.Content)?.event ?: return
        val resourceIdStr = event.resourceId ?: return
        val resourceName = event.resource.serialName

        when {
            resourceName.equals(UserAuditResourceType.IDENTIFIER.serialName, ignoreCase = true) -> {
                resourceIdStr.toUserIdentifierIdOrNull()?.let { id ->
                    onNavigateToIdentifierDetail?.invoke(id)
                }
            }
            resourceName.equals(UserAuditResourceType.SESSION.serialName, ignoreCase = true) -> {
                resourceIdStr.toUserSessionIdOrNull()?.let { id ->
                    onNavigateToSessionDetail?.invoke(id)
                }
            }
            resourceName.equals(UserAuditResourceType.USER.serialName, ignoreCase = true) -> {
                resourceIdStr.toUserIdOrNull()?.let { userId ->
                    if (currentUserId != null && userId == currentUserId) {
                        onNavigateToProfile?.invoke()
                    } else {
                        onNavigateToUserDetail?.invoke(userId)
                    }
                }
            }
        }
    }

    override fun onSubjectClick() {
        val event = (_state.value as? AuditEventDetailScreenState.Content)?.event ?: return
        val actorIdStr = event.actorId ?: return
        val isUserActor = event.actorType == AuditActorType.USER ||
                event.actorType.serialName.equals(AuditActorType.USER.serialName, ignoreCase = true)

        if (isUserActor) {
            actorIdStr.toUserIdOrNull()?.let { userId ->
                if (currentUserId != null && userId == currentUserId) {
                    onNavigateToProfile?.invoke()
                } else {
                    onNavigateToUserDetail?.invoke(userId)
                }
            }
        }
    }

    private fun loadEvent() {
        _state.value = AuditEventDetailScreenState.Loading
        scope.launch {
            getAuditEventUseCase(eventId.asHexDashString())
                .onSuccess { event ->
                    _state.value = AuditEventDetailScreenState.Content(event = event)
                }
                .onError { error ->
                    _state.value = AuditEventDetailScreenState.Error(error)
                }
        }
    }
}