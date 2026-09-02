package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.AppError
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult

/**
 * Encapsulates the state of a paginated list.
 *
 * @param T The type of items in the list.
 * @property items Cumulative list of items from all loaded pages.
 * @property isInitialLoading True when the first page is being fetched and the list is empty.
 * @property isNextPageLoading True when a subsequent page is being fetched.
 * @property error Error from the last attempt (initial or next page).
 * @property pageNumber One-based index of the last successfully loaded page.
 * @property totalPages Total number of pages available on the server.
 */
data class PaginationState<T>(
    val items: List<T> = emptyList(),
    val isInitialLoading: Boolean = false,
    val isNextPageLoading: Boolean = false,
    val error: AppError? = null,
    val pageNumber: Int = 0,
    val totalPages: Long = 0
) {
    /**
     * True if more items can be requested from the server.
     */
    val canLoadMore: Boolean get() = pageNumber < totalPages
            && !isNextPageLoading
            && !isInitialLoading
}

/**
 * Transitions state to initial loading (clears everything).
 */
fun <T> PaginationState<T>.toInitialLoading(): PaginationState<T> = copy(
    items = emptyList(),
    isInitialLoading = true,
    isNextPageLoading = false,
    error = null,
    pageNumber = 0,
    totalPages = 0
)

/**
 * Transitions state to next page loading (keeps current items).
 */
fun <T> PaginationState<T>.toNextPageLoading(): PaginationState<T> = copy(
    isInitialLoading = false,
    isNextPageLoading = true,
    error = null
)

/**
 * Updates state with a new page from [PagedResult].
 * Appends new items to the existing list.
 */
fun <T> PaginationState<T>.appendResult(result: PagedResult<T>): PaginationState<T> = copy(
    items = items + result.items,
    isInitialLoading = false,
    isNextPageLoading = false,
    error = null,
    pageNumber = result.pageNumber,
    totalPages = result.totalPages
)

/**
 * Transitions state to error.
 * [isInitial] defines if it was a failure during the first page fetch.
 */
fun <T> PaginationState<T>.toError(error: AppError, isInitial: Boolean): PaginationState<T> = copy(
    isInitialLoading = false,
    isNextPageLoading = false,
    error = error,
    items = if (isInitial)
        emptyList()
    else
        items
)
