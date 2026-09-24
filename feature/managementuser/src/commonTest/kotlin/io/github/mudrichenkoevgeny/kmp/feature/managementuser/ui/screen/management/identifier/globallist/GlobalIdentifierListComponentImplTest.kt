package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.identifier.globallist

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.filter.TextListingFilterState
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.sort.ListingSortState
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.identifier.ManagementIdentifierRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.identifier.UserIdentifier
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.listing.UserFilterValues
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@InternalApi
class GlobalIdentifierListComponentImplTest {

    @Test
    fun init_loadsIdentifiersSuccessfully() = runComponentTest {
        val identifier = userIdentifierMock()
        val context = createTestContext(identifiers = listOf(identifier))

        advanceUntilIdle()
        val state = assertIs<GlobalIdentifierListScreenState.Content>(context.component.state.value)
        assertEquals(listOf(identifier), state.paging.items)
    }

    @Test
    fun init_emitsErrorState_whenUseCaseFails() = runComponentTest {
        val repository = ManagementIdentifierRepositoryMock()
        val error = CommonError.Unknown()
        repository.getIdentifiersResultProvider = { AppResult.Error(error) }
        val context = createTestContext(repository = repository)

        advanceUntilIdle()
        val state = assertIs<GlobalIdentifierListScreenState.Error>(context.component.state.value)
        assertEquals(error, state.error)
    }

    @Test
    fun onRefresh_reloadsIdentifiers() = runComponentTest {
        val context = createTestContext()
        advanceUntilIdle()

        context.component.onRefresh()

        advanceUntilIdle()
        assertIs<GlobalIdentifierListScreenState.Content>(context.component.state.value)
    }

    @Test
    fun onBackClick_invokesCallback() = runComponentTest {
        val context = createTestContext()
        context.component.onBackClick()
        assertEquals(1, context.onBackCalls)
    }

    @Test
    fun onToggleFilterPanel_togglesExpandedState() = runComponentTest {
        val context = createTestContext(identifiers = emptyList())
        advanceUntilIdle()

        val initialState = assertIs<GlobalIdentifierListScreenState.Content>(context.component.state.value)
        assertFalse(initialState.isFilterPanelExpanded)

        context.component.onToggleFilterPanel()
        val expandedState = assertIs<GlobalIdentifierListScreenState.Content>(context.component.state.value)
        assertTrue(expandedState.isFilterPanelExpanded)

        context.component.onToggleFilterPanel()
        val collapsedState = assertIs<GlobalIdentifierListScreenState.Content>(context.component.state.value)
        assertFalse(collapsedState.isFilterPanelExpanded)
    }

    @Test
    fun onSortChanged_updatesSortState() = runComponentTest {
        val context = createTestContext(identifiers = emptyList())
        advanceUntilIdle()

        val newSortState = ListingSortState(optionId = "created_at", isAscending = true)
        context.component.onSortChanged(newSortState)

        val state = assertIs<GlobalIdentifierListScreenState.Content>(context.component.state.value)
        assertEquals(newSortState, state.sortState)
    }

    @Test
    fun onFilterChanged_updatesAndRemovesFilters() = runComponentTest {
        val context = createTestContext(identifiers = emptyList())
        advanceUntilIdle()

        val filterId = UserFilterValues.UserIdentifierFilterValues.IDENTIFIER
        val filterState = TextListingFilterState(value = "user@example.com")

        context.component.onFilterChanged(filterId = filterId, filterState = filterState)
        var state = assertIs<GlobalIdentifierListScreenState.Content>(context.component.state.value)
        assertEquals(filterState, state.filterStates[filterId])

        context.component.onFilterChanged(filterId = filterId, filterState = null)
        state = assertIs<GlobalIdentifierListScreenState.Content>(context.component.state.value)
        assertFalse(state.filterStates.containsKey(filterId))
    }

    @Test
    fun onApplyFilters_closesPanelAndReloadsIdentifiers() = runComponentTest {
        val context = createTestContext(identifiers = emptyList())
        advanceUntilIdle()

        context.component.onToggleFilterPanel()
        assertTrue(assertIs<GlobalIdentifierListScreenState.Content>(context.component.state.value).isFilterPanelExpanded)

        context.component.onApplyFilters()
        advanceUntilIdle()

        val state = assertIs<GlobalIdentifierListScreenState.Content>(context.component.state.value)
        assertFalse(state.isFilterPanelExpanded)
    }

    private fun createTestContext(
        identifiers: List<UserIdentifier>? = null,
        repository: ManagementIdentifierRepositoryMock = ManagementIdentifierRepositoryMock()
    ): TestContext {
        if (identifiers != null) {
            repository.getIdentifiersResultProvider = { AppResult.Success(pagedResultMock(identifiers)) }
        }

        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val useCase = ManagementGetIdentifiersUseCase(repository)
        val context = TestContext()

        context.component = GlobalIdentifierListComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            managementGetIdentifiersUseCase = useCase,
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class TestContext {
        lateinit var component: GlobalIdentifierListComponentImpl
        var onBackCalls = 0
    }
}
