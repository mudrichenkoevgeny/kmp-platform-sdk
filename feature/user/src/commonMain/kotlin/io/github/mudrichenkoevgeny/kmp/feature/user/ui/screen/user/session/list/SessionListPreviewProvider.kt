package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionSummaryList

/**
 * Preview parameter provider delivering various [SessionListScreenState] configurations for Compose previews.
 */
@InternalApi
public class SessionListPreviewProvider : PreviewParameterProvider<SessionListScreenState> {
    private val defaultSessions = createTestUserSessionSummaryList(count = 2)
    private val currentSession = defaultSessions.first()

    private val items: List<Pair<String, SessionListScreenState>> = listOf(
        "Current and Other Sessions" to SessionListScreenState.Content(
            paging = PaginationState(
                items = defaultSessions
            ),
            currentSessionId = currentSession.id
        ),
        "Multiple Sessions" to SessionListScreenState.Content(
            paging = PaginationState(
                items = createTestUserSessionSummaryList(count = 2)
            )
        ),
        "Single Session" to SessionListScreenState.Content(
            paging = PaginationState(
                items = listOf(currentSession)
            )
        ),
        "Action Loading" to SessionListScreenState.Content(
            paging = PaginationState(
                items = listOf(currentSession)
            ),
            actionLoading = true
        ),
        "Global Error" to SessionListScreenState.Error(error = CommonError.Unknown()),
        "Fullscreen Loading" to SessionListScreenState.Loading
    )

    override val values: Sequence<SessionListScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}
