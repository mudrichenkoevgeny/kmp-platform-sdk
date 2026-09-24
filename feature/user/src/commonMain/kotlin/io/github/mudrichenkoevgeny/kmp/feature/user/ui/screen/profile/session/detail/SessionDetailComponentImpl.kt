package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrNull
import kotlinx.coroutines.launch

/**
 * Default implementation of [SessionDetailComponent].
 *
 * Supports displaying session details and revoking active sessions across both end-user and administrative contexts.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param session Initial [UserSession] model if already available.
 * @param sessionId Target [UserSessionId] identifier.
 * @param fetchSession Suspend callback to fetch session details remotely when [session] is not provided.
 * @param revokeSession Suspend callback to revoke the target session.
 * @param isCurrentSession Indicates whether this session belongs to the current device.
 * @param authStorage Storage used to resolve active session ID if [isCurrentSession] is omitted.
 * @param onSessionRevoked Callback invoked when the session is successfully revoked.
 * @param onNavigateToIdentifierDetail Callback to navigate to identifier detail screen.
 * @param onBack Callback to pop this screen from the navigation stack.
 */
class SessionDetailComponentImpl(
    componentContext: ComponentContext,
    private val session: UserSession? = null,
    private val sessionId: UserSessionId? = session?.id,
    private val fetchSession: (suspend (UserSessionId) -> AppResult<UserSession>)? = null,
    private val revokeSession: (suspend (UserSessionId) -> AppResult<Unit>)? = null,
    private val isCurrentSession: Boolean? = null,
    private val authStorage: AuthStorage? = null,
    private val onSessionRevoked: ((UserSessionId) -> Unit)? = null,
    private val onNavigateToIdentifierDetail: ((UserIdentifierId) -> Unit)? = null,
    private val onBack: () -> Unit
) : SessionDetailComponent, ComponentContext by componentContext {

    /**
     * Convenience secondary constructor for standard self-session management workflows using UseCases.
     */
    constructor(
        componentContext: ComponentContext,
        session: UserSession? = null,
        sessionId: UserSessionId? = session?.id,
        getSessionUseCase: GetSessionUseCase? = null,
        deleteSessionUseCase: DeleteSessionUseCase? = null,
        isCurrentSession: Boolean? = null,
        authStorage: AuthStorage? = null,
        onSessionRevoked: ((UserSessionId) -> Unit)? = null,
        onNavigateToIdentifierDetail: ((UserIdentifierId) -> Unit)? = null,
        onBack: () -> Unit
    ) : this(
        componentContext = componentContext,
        session = session,
        sessionId = sessionId,
        fetchSession = getSessionUseCase?.let { useCase -> { id -> useCase(id) } },
        revokeSession = deleteSessionUseCase?.let { useCase -> { id -> useCase(id) } },
        isCurrentSession = isCurrentSession,
        authStorage = authStorage,
        onSessionRevoked = onSessionRevoked,
        onNavigateToIdentifierDetail = onNavigateToIdentifierDetail,
        onBack = onBack
    )

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<SessionDetailScreenState>(SessionDetailScreenState.Loading)
    override val state: Value<SessionDetailScreenState> = _state

    init {
        initializeSession()
    }

    override fun onRevokeSessionClick() {
        val current = _state.value as? SessionDetailScreenState.Content ?: return
        val targetSessionId = current.session.id
        _state.value = current.copy(actionLoading = true, actionError = null)

        scope.launch {
            val result = revokeSession?.invoke(targetSessionId) ?: AppResult.Success(Unit)
            result
                .onSuccess {
                    onSessionRevoked?.invoke(targetSessionId)
                    onBack()
                }
                .onError { error ->
                    _state.value = current.copy(actionLoading = false, actionError = error)
                }
        }
    }

    override fun onRetry() {
        initializeSession()
    }

    override fun onBackClick() {
        onBack()
    }

    override fun onIdentifierClick() {
        val current = _state.value as? SessionDetailScreenState.Content ?: return
        onNavigateToIdentifierDetail?.invoke(current.session.identifierId)
    }

    private fun initializeSession() {
        scope.launch {
            val targetId = sessionId ?: session?.id
            val activeSessionId = authStorage?.getSessionId()?.toUserSessionIdOrNull()
            val resolvedIsCurrentSession = isCurrentSession ?: (targetId != null && targetId == activeSessionId)

            if (session != null) {
                _state.value = SessionDetailScreenState.Content(
                    session = session,
                    isCurrentSession = resolvedIsCurrentSession
                )
            } else if (targetId != null && fetchSession != null) {
                _state.value = SessionDetailScreenState.Loading
                fetchSession(targetId)
                    .onSuccess { loadedSession ->
                        _state.value = SessionDetailScreenState.Content(
                            session = loadedSession,
                            isCurrentSession = resolvedIsCurrentSession
                        )
                    }
                    .onError { error ->
                        _state.value = SessionDetailScreenState.Error(error)
                    }
            } else {
                _state.value = SessionDetailScreenState.Error(CommonError.Unknown())
            }
        }
    }
}
