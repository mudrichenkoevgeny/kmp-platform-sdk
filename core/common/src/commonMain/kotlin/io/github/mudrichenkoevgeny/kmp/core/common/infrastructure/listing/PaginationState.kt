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
    val totalPages: Long = 0,
) {
    /**
     * One-based index of the next page to fetch.
     */
    val nextPageNumber: Int
        get() = if (pageNumber == 0) ListingConstants.INITIAL_PAGE_NUMBER else pageNumber + 1

    /**
     * True if the list contains no items.
     */
    val isEmpty: Boolean get() = items.isEmpty()

    /**
     * True if the list contains one or more items.
     */
    val isNotEmpty: Boolean get() = items.isNotEmpty()

    /**
     * True if an error occurred during the last fetch attempt.
     */
    val hasError: Boolean get() = error != null

    /**
     * True if more pages are available on the server.
     */
    val hasMorePages: Boolean get() = pageNumber < totalPages

    /**
     * True if no fetch operation is currently in progress.
     */
    val isIdle: Boolean get() = !isInitialLoading && !isNextPageLoading

    /**
     * True if a subsequent page can be fetched from the server.
     */
    val canLoadMore: Boolean get() = hasMorePages && isIdle
}

/**
 * Transitions state to initial loading.
 */
fun <T> PaginationState<T>.toInitialLoading(): PaginationState<T> = copy(
    items = emptyList(),
    isInitialLoading = true,
    isNextPageLoading = false,
    error = null,
    pageNumber = 0,
    totalPages = 0,
)

/**
 * Transitions state to next page loading.
 */
fun <T> PaginationState<T>.toNextPageLoading(): PaginationState<T> = copy(
    isInitialLoading = false,
    isNextPageLoading = true,
    error = null,
)

/**
 * Updates state with a new page from [PagedResult].
 */
fun <T> PaginationState<T>.appendResult(result: PagedResult<T>): PaginationState<T> = copy(
    items = items + result.items,
    isInitialLoading = false,
    isNextPageLoading = false,
    error = null,
    pageNumber = result.pageNumber,
    totalPages = result.totalPages,
)

/**
 * Transitions state to error.
 */
fun <T> PaginationState<T>.toError(error: AppError, isInitial: Boolean): PaginationState<T> = copy(
    isInitialLoading = false,
    isNextPageLoading = false,
    error = error,
    items = if (isInitial) emptyList() else items,
)

/**
 * Transitions state to error for a requested [pageNumber].
 * Automatically determines if it was the initial page fetch.
 */
fun <T> PaginationState<T>.toError(error: AppError, pageNumber: Int): PaginationState<T> = toError(
    error = error,
    isInitial = pageNumber == ListingConstants.INITIAL_PAGE_NUMBER,
)
