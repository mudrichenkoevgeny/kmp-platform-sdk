package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierSummary

/**
 * Preview parameter provider delivering various [IdentifierListScreenState] configurations for Compose previews.
 */
@InternalApi
public class IdentifierListPreviewProvider : PreviewParameterProvider<IdentifierListScreenState> {
    private val items: List<Pair<String, IdentifierListScreenState>> = listOf(
        "Content List" to IdentifierListScreenState.Content(
            paging = PaginationState(
                items = listOf(createTestUserIdentifierSummary())
            ),
            isAddIdentifierSupported = true
        ),
        "Action Loading" to IdentifierListScreenState.Content(
            paging = PaginationState(items = listOf(createTestUserIdentifierSummary())),
            isAddIdentifierSupported = true,
            actionLoading = true
        ),
        "Global Error" to IdentifierListScreenState.Error(error = CommonError.Unknown()),
        "Fullscreen Loading" to IdentifierListScreenState.Loading
    )

    override val values: Sequence<IdentifierListScreenState> = items.asSequence().map { it.second }

    override fun getDisplayName(index: Int): String? = items.getOrNull(index)?.first
}
