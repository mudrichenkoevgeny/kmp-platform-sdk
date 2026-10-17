package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackDispatcher
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.componentCoroutineScope
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onError
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionUseCase
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifierId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.toUserSessionIdOrNull
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.detail.SessionDetailParams
import kotlinx.coroutines.launch

/**
 * Default implementation of [SessionDetailComponent].
 *
 * Supports displaying session details and revoking active sessions across both end-user and administrative contexts.
 *
 * @param componentContext Decompose [ComponentContext].
 * @param params Target session parameters.
 * @param userRepository Source of the current user profile state.
 * @param fetchSession Suspend callback to fetch session details remotely.
 * @param revokeSession Suspend callback to revoke the target session.
 * @param isCurrentSession Indicates whether this session belongs to the current device.
 * @param authStorage Storage used to resolve active session ID if [isCurrentSession] is omitted.
 * @param onSessionRevoked Callback invoked when the session is successfully revoked.
 * @param onNavigateToIdentifierDetail Callback to navigate to identifier detail screen.
 * @param onNavigateToUserDetail Callback to navigate to user detail screen.
 * @param onNavigateToProfile Callback to navigate to main profile screen.
 * @param onBack Callback to pop this screen from the navigation stack.
 */
class SessionDetailComponentImpl(
    componentContext: ComponentContext,
    private val params: SessionDetailParams,
    private val userRepository: UserRepository? = null,
    private val fetchSession: (suspend (UserSessionId) -> AppResult<UserSessionPrivate>)? = null,
    private val revokeSession: (suspend (UserSessionId) -> AppResult<Unit>)? = null,
    private val isCurrentSession: Boolean? = null,
    private val authStorage: AuthStorage? = null,
    private val onSessionRevoked: ((UserSessionId) -> Unit)? = null,
    private val onNavigateToIdentifierDetail: ((UserIdentifierId) -> Unit)? = null,
    private val onNavigateToUserDetail: ((UserId) -> Unit)? = null,
    private val onNavigateToProfile: () -> Unit,
    private val onBack: () -> Unit
) : SessionDetailComponent, ComponentContext by componentContext {

    /**
     * Convenience secondary constructor for standard self-session management workflows using UseCases.
     */
    constructor(
        componentContext: ComponentContext,
        params: SessionDetailParams,
        userRepository: UserRepository? = null,
        getSessionUseCase: GetSessionUseCase? = null,
        deleteSessionUseCase: DeleteSessionUseCase? = null,
        isCurrentSession: Boolean? = null,
        authStorage: AuthStorage? = null,
        onSessionRevoked: ((UserSessionId) -> Unit)? = null,
        onNavigateToIdentifierDetail: ((UserIdentifierId) -> Unit)? = null,
        onNavigateToUserDetail: ((UserId) -> Unit)? = null,
        onNavigateToProfile: () -> Unit,
        onBack: () -> Unit
    ) : this(
        componentContext = componentContext,
        params = params,
        userRepository = userRepository,
        fetchSession = getSessionUseCase?.let { useCase -> { id -> useCase(id) } },
        revokeSession = deleteSessionUseCase?.let { useCase -> { id -> useCase(id) } },
        isCurrentSession = isCurrentSession,
        authStorage = authStorage,
        onSessionRevoked = onSessionRevoked,
        onNavigateToIdentifierDetail = onNavigateToIdentifierDetail,
        onNavigateToUserDetail = onNavigateToUserDetail,
        onNavigateToProfile = onNavigateToProfile,
        onBack = onBack
    )

    private val sessionId: UserSessionId? get() = params.typedSessionId

    private val scope = componentCoroutineScope()
    private val _state = MutableValue<SessionDetailScreenState>(SessionDetailScreenState.Loading)
    override val state: Value<SessionDetailScreenState> = _state

    private var currentUserId: UserId? = null

    init {
        scope.launch {
            userRepository?.currentUser?.collect { user ->
                currentUserId = user?.id
            }
        }
        initializeSession()
    }

    override fun onRevokeSessionClick() {
        val current = _state.value as? SessionDetailScreenState.Content ?: return
        val targetId = sessionId ?: current.session.id
        if (revokeSession == null) {
            return
        }

        _state.value = current.copy(actionLoading = true, actionError = null)
        scope.launch {
            revokeSession.invoke(targetId)
                .onSuccess {
                    onSessionRevoked?.invoke(targetId)
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
        if ((backHandler as? BackDispatcher)?.back() != true) {
            onBack()
        }
    }

    override fun onIdentifierClick() {
        val current = _state.value as? SessionDetailScreenState.Content ?: return
        onNavigateToIdentifierDetail?.invoke(current.session.identifierId)
    }

    override fun onUserClick() {
        val current = _state.value as? SessionDetailScreenState.Content ?: return
        if (onNavigateToUserDetail != null) {
            onNavigateToUserDetail.invoke(current.session.userId)
        } else {
            onNavigateToProfile()
        }
    }

    private fun initializeSession() {
        scope.launch {
            val targetId = sessionId
            val activeSessionId = authStorage?.getSessionId()?.toUserSessionIdOrNull()
            val resolvedIsCurrentSession = isCurrentSession ?: (targetId != null && targetId == activeSessionId)

            if (targetId != null && fetchSession != null) {
                _state.value = SessionDetailScreenState.Loading
                fetchSession(targetId)
                    .onSuccess { loadedSession ->
                        val isSelf = currentUserId != null && loadedSession.userId == currentUserId
                        _state.value = SessionDetailScreenState.Content(
                            session = loadedSession,
                            isCurrentSession = resolvedIsCurrentSession,
                            isSelfSession = isSelf
                        )
                    }
                    .onError { error ->
                        _state.value = SessionDetailScreenState.Error(error)
                    }
            } else {
                _state.value = SessionDetailScreenState.Error(
                    CommonError.ContractViolation(throwable = IllegalStateException("Invalid sessionId in params"))
                )
            }
        }
    }
}
