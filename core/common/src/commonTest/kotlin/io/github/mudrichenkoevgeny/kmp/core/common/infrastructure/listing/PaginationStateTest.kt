package io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@InternalApi
class PaginationStateTest {

    @Test
    fun nextPageNumber_returnsInitialPageNumber_whenPageNumberIsZero() {
        val state = PaginationState<String>(pageNumber = 0)
        assertEquals(ListingConstants.INITIAL_PAGE_NUMBER, state.nextPageNumber)
    }

    @Test
    fun nextPageNumber_returnsIncrementedPageNumber_whenPageNumberIsGreaterThanZero() {
        val state = PaginationState<String>(pageNumber = 1)
        assertEquals(2, state.nextPageNumber)
    }

    @Test
    fun isEmpty_and_isNotEmpty_reflectItemsPresence() {
        val emptyState = PaginationState<String>(items = emptyList())
        assertTrue(emptyState.isEmpty)
        assertFalse(emptyState.isNotEmpty)

        val nonEmptyState = PaginationState(items = listOf("item"))
        assertFalse(nonEmptyState.isEmpty)
        assertTrue(nonEmptyState.isNotEmpty)
    }

    @Test
    fun hasError_returnsTrue_whenErrorIsNotNull() {
        val noErrorState = PaginationState<String>()
        assertFalse(noErrorState.hasError)

        val errorState = PaginationState<String>(error = CommonError.Unknown())
        assertTrue(errorState.hasError)
    }

    @Test
    fun hasMorePages_returnsTrue_whenPageNumberIsLessThanTotalPages() {
        val hasMoreState = PaginationState<String>(pageNumber = 1, totalPages = 2)
        assertTrue(hasMoreState.hasMorePages)

        val noMoreState = PaginationState<String>(pageNumber = 2, totalPages = 2)
        assertFalse(noMoreState.hasMorePages)
    }

    @Test
    fun isIdle_returnsTrue_whenNotLoading() {
        val idleState = PaginationState<String>(isInitialLoading = false, isNextPageLoading = false)
        assertTrue(idleState.isIdle)

        val initialLoadingState = PaginationState<String>(isInitialLoading = true)
        assertFalse(initialLoadingState.isIdle)

        val nextPageLoadingState = PaginationState<String>(isNextPageLoading = true)
        assertFalse(nextPageLoadingState.isIdle)
    }

    @Test
    fun canLoadMore_returnsTrue_whenHasMorePagesAndIsIdle() {
        val canLoadState = PaginationState<String>(pageNumber = 1, totalPages = 2, isInitialLoading = false, isNextPageLoading = false)
        assertTrue(canLoadState.canLoadMore)

        val loadingState = PaginationState<String>(pageNumber = 1, totalPages = 2, isNextPageLoading = true)
        assertFalse(loadingState.canLoadMore)

        val lastPageState = PaginationState<String>(pageNumber = 2, totalPages = 2)
        assertFalse(lastPageState.canLoadMore)
    }

    @Test
    fun toInitialLoading_clearsItemsAndSetsLoading() {
        val state = PaginationState(items = listOf("item"), pageNumber = 1, totalPages = 2)
        val loadingState = state.toInitialLoading()

        assertTrue(loadingState.items.isEmpty())
        assertTrue(loadingState.isInitialLoading)
        assertFalse(loadingState.isNextPageLoading)
        assertNull(loadingState.error)
        assertEquals(0, loadingState.pageNumber)
        assertEquals(0L, loadingState.totalPages)
    }

    @Test
    fun toNextPageLoading_retainsItemsAndSetsLoading() {
        val state = PaginationState(items = listOf("item"), pageNumber = 1, totalPages = 2)
        val loadingState = state.toNextPageLoading()

        assertEquals(listOf("item"), loadingState.items)
        assertFalse(loadingState.isInitialLoading)
        assertTrue(loadingState.isNextPageLoading)
        assertNull(loadingState.error)
    }

    @Test
    fun appendResult_appendsItemsAndUpdatesPagination() {
        val initialState = PaginationState(items = listOf("item1"), pageNumber = 1, totalPages = 2)
        val pagedResult = pagedResultMock(listOf("item2"), pageNumber = 2, totalPages = 2)

        val resultState = initialState.appendResult(pagedResult)

        assertEquals(listOf("item1", "item2"), resultState.items)
        assertFalse(resultState.isInitialLoading)
        assertFalse(resultState.isNextPageLoading)
        assertNull(resultState.error)
        assertEquals(2, resultState.pageNumber)
        assertEquals(2L, resultState.totalPages)
    }

    @Test
    fun toError_clearsItemsOnInitialError_andRetainsItemsOnNextPageError() {
        val initialError = CommonError.Unknown()
        val stateWithItems = PaginationState(items = listOf("item1"))

        val initialErrorState = stateWithItems.toError(initialError, isInitial = true)
        assertTrue(initialErrorState.items.isEmpty())
        assertEquals(initialError, initialErrorState.error)

        val nextPageErrorState = stateWithItems.toError(initialError, isInitial = false)
        assertEquals(listOf("item1"), nextPageErrorState.items)
        assertEquals(initialError, nextPageErrorState.error)
    }

    @Test
    fun toError_withPageNumber_automaticallyDeterminesInitialStatus() {
        val error = CommonError.Unknown()
        val stateWithItems = PaginationState(items = listOf("item1"))

        val initialErrorState = stateWithItems.toError(error, pageNumber = ListingConstants.INITIAL_PAGE_NUMBER)
        assertTrue(initialErrorState.items.isEmpty())
        assertEquals(error, initialErrorState.error)

        val nextPageErrorState = stateWithItems.toError(error, pageNumber = 2)
        assertEquals(listOf("item1"), nextPageErrorState.items)
        assertEquals(error, nextPageErrorState.error)
    }
}
